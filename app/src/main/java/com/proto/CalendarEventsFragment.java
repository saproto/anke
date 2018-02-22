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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class CalendarEventsFragment extends Fragment {
    //RANDOM COMMENT
    public CalendarEventsFragment() {
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
  /*      ContentValues values = new ContentValues();
        values.put(CalendarProvider.COLOR, Event.COLOR_GREEN);
        values.put(CalendarProvider.DESCRIPTION, "Some Description");
        values.put(CalendarProvider.LOCATION, "Some location");
                values.put(CalendarProvider.EVENT, "Event name");

                        Calendar cal = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        cal.set(2017, 11, 19, 22, 00);
        int julianDay = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));
        values.put(CalendarProvider.START, cal.getTimeInMillis());
        values.put(CalendarProvider.START_DAY, 2458090);


        cal.set(2017, 11, 20, 22, 00);
        int endDayJulian = Time.getJulianDay(cal.getTimeInMillis(), TimeUnit.MILLISECONDS.toSeconds(tz.getOffset(cal.getTimeInMillis())));

        values.put(CalendarProvider.END, cal.getTimeInMillis());
        values.put(CalendarProvider.END_DAY, 2458091);

        Uri uri = getContext().getContentResolver().insert(CalendarProvider.CONTENT_URI, values); */
        //calendar=((CalendarView)v.findViewById(R.id.calendarView));
       /* calendar.setOnDayClickListener(new  ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void OnDayClickListener(CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(v.getContext(), dayOfMonth + "/" + month  + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });*/
        return v;
    }


}
