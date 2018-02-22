package com.proto;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;
import java.util.ArrayList;



public class CalendarEventsFragment  extends Fragment {

    public CalendarEventsFragment() {

    }

    ExtendedCalendarView calendar;

    ArrayList<Event> eventList = new ArrayList<Event>();


    public interface OnDaySelectedListener {
        void onDaySelected(Day day);
    }

    private OnDaySelectedListener sDummyCallbacks = new OnDaySelectedListener() {
        @Override
        public void onDaySelected(Day day) {
        }
    };

   private OnDaySelectedListener mListener;




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

//          if (context instanceof OnDaySelectedListener) {
            mListener = (OnDaySelectedListener) context;
            //    }
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement OnDaySelectedListener");

        }
//
    }


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
        //listContainer = getListView();
        calendar = (ExtendedCalendarView) v.findViewById(R.id.calendar);
        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {

                Intent intent = new Intent(getActivity(), PopUpWindow.class);
                int numEvent = day.getNumOfEvenets();
                ArrayList<String> titles = new ArrayList<String>();
                ArrayList<String> startDate = new ArrayList<String>();
                ArrayList<String> endDate = new ArrayList<String>();
                ArrayList<String> location = new ArrayList<String>();
                ArrayList<String> description = new ArrayList<String>();
                for(int i = 0; i<numEvent; i++){

                    Event dagEvent = day.getEvents().get(i);
                    titles.add(dagEvent.getTitle());
                    startDate.add(dagEvent.getStartDate());
                    endDate.add(dagEvent.getEndDate());
                    location.add(dagEvent.getLocation());
                    description.add(dagEvent.getDescription());
                }


                intent.putExtra("numEvent", numEvent);
                intent.putExtra("title",titles);
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate",endDate);
                intent.putExtra("location",location);
                intent.putExtra("description",description);
                startActivity(intent);
            }

            ;

        });


        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mListener = sDummyCallbacks;
    }



}