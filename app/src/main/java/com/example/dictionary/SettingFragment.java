package com.example.dictionary;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class SettingFragment extends Fragment {
    public TextView birthText;
    public EditText userId;
    public Button submitbtn;
    public FirebaseAuth firebaseAuth;
    public FragmentManager fragmentManager;
    public SelectbirthdayFragment selectbirthdayFragment;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef1 = database.getReference();

    public SettingFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        fragmentManager = getChildFragmentManager();
        selectbirthdayFragment = new SelectbirthdayFragment();
        birthText = v.findViewById(R.id.select_birth);
        submitbtn = v.findViewById(R.id.button_submit);
        userId = v.findViewById(R.id.input_id);

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

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = userId.getText().toString();
                String birth = birthText.getText().toString();
                String profile = "";
                String uid = user.getUid();

                myRef1.child("User").child(uid).child("uid").setValue(uid);
                myRef1.child("User").child(uid).child("profile").setValue(profile);
                myRef1.child("User").child(uid).child("id").setValue(id);
                myRef1.child("User").child(uid).child("birth").setValue(birth);
            }
        });

        return v;
    }


}
