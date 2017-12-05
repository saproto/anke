package com.proto;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.List;


public class CalendarEventsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<CalendarEntry>>  {



    public CalendarEventsFragment() {
        // Required empty public constructor
    }

    ExtendedCalendarView calendar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
           // Inflate the layout for this fragment
       final View v = inflater.inflate(R.layout.fragment_calendar_events, container, false);
        super.onViewCreated(v, savedInstanceState);
//        Button mShowDialog = (Button)v.findViewById(R.id.button);
//        mShowDialog.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                View mView = getLayoutInflater(savedInstanceState).inflate(R.layout.popup_event,null);
//
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();
//            }
       // });


       // listContainer = getListView();
        calendar = (ExtendedCalendarView)v.findViewById(R.id.calendar);


        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {

                Event dagEvent = day.getEvents().get(0);
                showPopup(savedInstanceState);
//                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//                View mView = getLayoutInflater(savedInstanceState).inflate(R.layout.popup_event,null);
//
//                mBuilder.setView(mView);
//                AlertDialog dialog = mBuilder.create();
//                dialog.show();

                // day.getEvents().toArray().toString();
                //.makeText(v.getContext(), "Er is geklikt, event op dag: " + day.getDay() + " is " + dagEvent.getDescription() ,Toast.LENGTH_LONG).show();
                //   Toast.makeText(v.getContext(), dayOfMonth + "/" + month  + "/" + year, Toast.LENGTH_SHORT).show();
                //Day dayclicked = day;
            };
        });
      //  setListAdapter(adapter);
        return v;
    }


    @Override
    public Loader<List<CalendarEntry>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<CalendarEntry>> loader, List<CalendarEntry> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<CalendarEntry>> loader) {

    }

    public void showPopup(Bundle savedInstanceState){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater(savedInstanceState).inflate(R.layout.popup_event,null);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
}
