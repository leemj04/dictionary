package com.example.dictionary;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Calendar;
import java.util.Date;

public class SettingFragment extends Fragment {
    public TextView birthText;
    public FragmentManager fragmentManager;
    public SelectbirthdayFragment selectbirthdayFragment;
    public SettingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        fragmentManager = getChildFragmentManager();
        selectbirthdayFragment = new SelectbirthdayFragment();
        birthText = v.findViewById(R.id.select_birth);

        selectbirthdayFragment.listener = new SelectbirthdayFragment.onClickListener() {
            @Override
            public void onClick(String s) {
                fragmentManager.beginTransaction().remove(selectbirthdayFragment).commit();
                birthText.setText(s);
            }
        };

        birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction().replace(R.id.profile_framelayout, selectbirthdayFragment).commit();
            }
        });
        return v;
    }


}
