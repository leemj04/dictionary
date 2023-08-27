package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

public class SelectbirthdayFragment extends Fragment {
    private FragmentManager fragmentManager;
    private Button submitbtn;
    public CalendarView calendarView;
    public Calendar calendar = Calendar.getInstance();
    public SelectbirthdayFragment() { }
    public interface onClickListener{
        public void onClick(String s);
    }
    static onClickListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selectbirthday, container, false);

        submitbtn = v.findViewById(R.id.button_confirm);
        calendarView = v.findViewById(R.id.calendar);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                calendar.set(year, month, day);
                calendarView.setDate(calendar.getTimeInMillis());
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(calendarView.getDate());
                calendar.setTime(date);
                String caldate = Integer.toString(calendar.get(Calendar.YEAR))+ "/" +
                        Integer.toString(calendar.get(Calendar.MONTH) + 1) + "/" +Integer.toString(calendar.get(Calendar.DATE));
                listener.onClick(caldate);
            }
        });

        return v;
    }

}