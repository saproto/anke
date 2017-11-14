package com.proto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

public class CalendarEventsFragment extends Fragment {
    //RANDOM COMMENT
    public CalendarEventsFragment() {
        // Required empty public constructor
    }

    CalendarView calendar;

    public static CalendarEventsFragment newInstance() {
        CalendarEventsFragment fragment = new CalendarEventsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
           // Inflate the layout for this fragment
       final View v = inflater.inflate(R.layout.fragment_calendar_events, container, false);
        calendar=((CalendarView)v.findViewById(R.id.calendarView));
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(v.getContext(), dayOfMonth + "/" + month  + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
        return v;



    }
}
