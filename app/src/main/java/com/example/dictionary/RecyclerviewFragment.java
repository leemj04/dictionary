package com.example.dictionary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class RecyclerviewFragment extends Fragment {
    RecyclerView recyclerView;
    UserAdapter adapter;
    EditText searchText;


    public RecyclerviewFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        recyclerView = v.findViewById(R.id.user_recyclerview);
        searchText = v.findViewById(R.id.user_find);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new UserAdapter();

        for(int i=0;i<10;i++) {
            adapter.addItem(new UserAdapter.Item("UserID"+i, "2023/08/25"));
        }

        recyclerView.setAdapter(adapter);

        return v;
    }
}