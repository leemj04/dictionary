package com.example.dictionary;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class IteminfoFragment extends Fragment {

    TextView userId, userBirth;
    Button backbtn;
    public IteminfoFragment() { }

    public interface onClickListener{
        public void onClick();
    }
    static onClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_iteminfo, container, false);

        userId = v.findViewById(R.id.id_detail);
        userBirth = v.findViewById(R.id.birthday_detail);
        backbtn = v.findViewById(R.id.button_back);

        getParentFragmentManager().setFragmentResultListener("data", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String key, @NonNull Bundle bundle) {
                String userid = bundle.getString("userid");
                String userbirth = bundle.getString("userbirth");

                userId.setText(userid);
                userBirth.setText(userbirth);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick();
            }
        });

        return v;
    }
}