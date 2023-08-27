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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerviewFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    EditText searchText;
    ArrayList<UserAdapter.Item> dataList = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");
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

        adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(UserAdapter.Item item) {
                Bundle bundle = new Bundle();
                bundle.putString("userid", item.getId());
                bundle.putString("userbirth", item.getBirth());

                fragmentManager = getParentFragmentManager();
                fragmentManager.setFragmentResult("data", bundle);
                fragmentManager.beginTransaction().replace(R.id.main_frame, iteminfoFragment).commit();
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.items.clear();
                dataList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String id = ds.child("id").getValue().toString();
                    String birth = ds.child("birth").getValue().toString();
                    String uid = ds.child("uid").getValue().toString();

                    if (id != null && birth != null && uid != null) {
                        UserAdapter.Item item = new UserAdapter.Item(id, birth, uid);
                        dataList.add(item);
                        adapter.addItem(item);
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