package com.proto;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalendarEventsFragment extends Fragment {
    //RANDOM COMMENT

    public CalendarEventsFragment() {


        ;
        // Required empty public constructor
    }

    ExtendedCalendarView calendar;

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
        calendar = (ExtendedCalendarView)v.findViewById(R.id.calendar);

        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {
                Event dagEvent = day.getEvents().get(0);

                // day.getEvents().toArray().toString();
                Toast.makeText(v.getContext(), "Er is geklikt, event op dag: " + day.getDay() + " is " + dagEvent.getDescription() ,Toast.LENGTH_LONG).show();
                //   Toast.makeText(v.getContext(), dayOfMonth + "/" + month  + "/" + year, Toast.LENGTH_SHORT).show();
                Day dayclicked = day;
            };
        });

        return v;
    }


}
