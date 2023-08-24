package com.example.dictionary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;

public class RecyclerviewFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    EditText searchText;
    ArrayList<UserAdapter.Item> dataList = new ArrayList<>();

    public RecyclerviewFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = v.findViewById(R.id.user_recyclerview);
        searchText = v.findViewById(R.id.user_find);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();

        for(int i=0;i<=10;i++) {
            dataList.add(new UserAdapter.Item("UserID"+i, "2023/08/25"));
            adapter.addItem(new UserAdapter.Item("UserID"+i, "2023/08/25"));
        }
        recyclerView.setAdapter(adapter);

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