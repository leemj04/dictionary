package com.example.dictionary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerviewFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter = new UserAdapter();
    EditText searchText;
    ArrayList<UserAdapter.Item> dataList = new ArrayList<>();
    ArrayList<String> friendList = new ArrayList<>();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("User");
    DatabaseReference friends = database.getReference("friends");
    FragmentManager fragmentManager;
    IteminfoFragment iteminfoFragment = new IteminfoFragment();
    String text = "";

    ChildEventListener friendsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String uid = snapshot.getKey();
            System.out.println("frinedsAdded:"+uid);
            friendList.add(uid);
            adapter.changeItemStar(uid);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String uid = snapshot.getKey();
            friendList.remove(uid);
            adapter.changeItemStar(uid);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

        @Override
        public void onCancelled(@NonNull DatabaseError error) {}
    };

    ChildEventListener myRefListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String id = snapshot.child("id").getValue().toString();
            String birth = snapshot.child("birth").getValue().toString();
            String uid = snapshot.child("uid").getValue().toString();
            if(!id.contains(text)) return;
            if(friendList.contains(uid)){
                adapter.addItem(new UserAdapter.Item(id, birth, uid, true));
            } else{
                adapter.addItem(new UserAdapter.Item(id, birth, uid, false));
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String id = snapshot.child("id").getValue().toString();
            String birth = snapshot.child("birth").getValue().toString();
            String uid = snapshot.child("uid").getValue().toString();
            if(!id.contains(text)) return;
            adapter.changeItem(uid, new UserAdapter.Item(id, birth, uid, friendList.contains(uid)));
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String uid = snapshot.child("uid").getValue().toString();
            String id = snapshot.child("id").getValue().toString();
            if(!id.contains(text)) return;
            adapter.removeItem(uid);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public RecyclerviewFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = v.findViewById(R.id.user_recyclerview);
        searchText = v.findViewById(R.id.user_find);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

            public void onStarClicked(UserAdapter.Item item, int position) {
                if (item.getStar()) {
                    friends.child(user.getUid()).child(item.getUid()).removeValue();
                } else {
                    friends.child(user.getUid()).child(item.getUid()).setValue(true);
                }
                myRef.child(user.getUid()).child("update").setValue("1");
                myRef.child(user.getUid()).child("update").setValue("0");
            }
        });



        return v;
    }

    TextWatcher textwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            friends.child(user.getUid()).removeEventListener(friendsListener);
            myRef.removeEventListener(myRefListener);
            friendList.clear();
            adapter.items.clear();
            adapter.starNum = 0;
            adapter.notifyDataSetChanged();
            text = s.toString();
            friends.child(user.getUid()).addChildEventListener(friendsListener);
            myRef.addChildEventListener(myRefListener);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        friends.child(user.getUid()).addChildEventListener(friendsListener);
        myRef.addChildEventListener(myRefListener);
        searchText.addTextChangedListener(textwatcher);
    }

    @Override
    public void onStop() {
        super.onStop();
        friends.child(user.getUid()).removeEventListener(friendsListener);
        myRef.removeEventListener(myRefListener);
        friendList.clear();
        adapter.items.clear();
        adapter.starNum = 0;
        adapter.notifyDataSetChanged();
        searchText.removeTextChangedListener(textwatcher);
    }
}