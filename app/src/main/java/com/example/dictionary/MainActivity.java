package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    RecyclerviewFragment recyclerviewFragment = new RecyclerviewFragment();
    SettingFragment settingFragment = new SettingFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.main_bottom);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frame,recyclerviewFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                fragmentManager = getSupportFragmentManager();

                if(id == R.id.bottom_home){
                    fragmentManager.beginTransaction().replace(R.id.main_frame, recyclerviewFragment)
                            .commit();
                } else if(id == R.id.bottom_menu){

                } else if(id == R.id.bottom_setting){
                    fragmentManager.beginTransaction().replace(R.id.main_frame, settingFragment)
                            .commit();
                } else if(id == R.id.bottom_info){

                }

                return true;
            }
        });
    }

}