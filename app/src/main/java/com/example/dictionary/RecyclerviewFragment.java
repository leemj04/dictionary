package com.example.dictionary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerviewFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    EditText searchText;
    ArrayList<UserAdapter.Item> dataList = new ArrayList<>();
    ArrayList<String> friendList = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");
    DatabaseReference friends = database.getReference("friends");
    FragmentManager fragmentManager;
    IteminfoFragment iteminfoFragment = new IteminfoFragment();

    public RecyclerviewFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = v.findViewById(R.id.user_recyclerview);
        searchText = v.findViewById(R.id.user_find);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();
        UserAdapter.context = getContext();
        recyclerView.setAdapter(adapter);

        iteminfoFragment.listener = new IteminfoFragment.onClickListener() {
            @Override
            public void onClick() {
                fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().remove(iteminfoFragment).commit();
            }
        };

        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(UserAdapter.Item item) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", item.getId());
                bundle.putString("userbirth", item.getBirth());

                fragmentManager = getChildFragmentManager();
                fragmentManager.setFragmentResult("data", bundle);
                fragmentManager.beginTransaction().replace(R.id.item_fragment, iteminfoFragment).commit();
            }
        });

        adapter.setOnStarClickListener(new UserAdapter.OnStarClickListener() {
            @Override
            public void onStartClicked(UserAdapter.Item item) {
                if (item.getStar()) {
                    friends.child(user.getUid()).child(item.getUid()).removeValue();
                    myRef.child(user.getUid()).child("update").setValue("1");
                    myRef.child(user.getUid()).child("update").setValue("0");
                } else {
                    friends.child(user.getUid()).child(item.getUid()).setValue(true);
                    myRef.child(user.getUid()).child("update").setValue("1");
                    myRef.child(user.getUid()).child("update").setValue("0");
                }
            }
        });

        friends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList.clear();
                for (DataSnapshot ds: snapshot.child(user.getUid()).getChildren()) {
                    String friendUid = ds.getKey();
                    friendList.add(friendUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                adapter.items.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (friendList.contains(ds.getKey())) {
                        String id = ds.child("id").getValue().toString();
                        String birth = ds.child("birth").getValue().toString();
                        String uid = ds.child("uid").getValue().toString();
                        UserAdapter.Item item = new UserAdapter.Item(id, birth, uid, true);

                        adapter.addItem(item);
                        dataList.add(item);

                        Log.e("X", ds.getKey());
                    }
                }

                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (!friendList.contains(ds.getKey())) {
                        String id = ds.child("id").getValue().toString();
                        String birth = ds.child("birth").getValue().toString();
                        String uid = ds.child("uid").getValue().toString();
                        UserAdapter.Item item = new UserAdapter.Item(id, birth, uid, false);

                        adapter.addItem(item);
                        dataList.add(item);

                        Log.e("Y", ds.getKey());
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                adapter.items.clear();
                for(UserAdapter.Item item: dataList){
                    if(item.id.contains(text)){
                        adapter.addItem(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        return v;
    }
}