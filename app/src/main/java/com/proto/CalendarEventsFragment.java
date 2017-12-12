package com.proto;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class CalendarEventsFragment extends Fragment {


 //   ArrayList<Event> eventList = new ArrayList<Event>();
    ListView listEvent;

    public CalendarEventsFragment() {
        // Required empty public constructor
    }

    ExtendedCalendarView calendar;
    ArrayList<Event> eventList = new ArrayList<Event>();

    public interface OnDaySelectedListener{
         void onDaySelected(Day day);
    }
     OnDaySelectedListener mListener;

  @Override
  public void onAttach(Context context){
      super.onAttach(context);
      try{
          if (context instanceof OnDaySelectedListener) {
              mListener = (OnDaySelectedListener) context;
          }
        } catch (ClassCastException e){

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
        calendar = (ExtendedCalendarView)v.findViewById(R.id.calendar);


        calendar.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {
            @Override
            public void onDayClicked(AdapterView<?> adapter, View view, int position, long id, Day day) {

                Log.i(TAG, "arrylist gemaakt");
                int numEvent = day.getNumOfEvenets();
                Log.i(TAG, "numEvents" + numEvent);
                for(int i = 0; i< numEvent; i++) {
                    eventList.add(day.getEvents().get(i));
                    Log.i(TAG, "in de for loop" + i);
                }
                if(numEvent != 0){
                mListener.onDaySelected(day);}

                //listEvent.setAdapter(new CalendarEntry.CalendarListAdapterSQL(getActivity().getApplication(),R.layout.row_popup_event,eventList));



            };

        });
      //  setListAdapter(adapter);
       // showPopup(savedInstanceState);
        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        Toast.makeText(getActivity().getApplication(),"hoi ik ben on pause",Toast.LENGTH_LONG).show();
    }
    public void showPopup(Bundle savedInstanceState){


    }

class PopUpWindow extends AsyncTask<Void,Event,Void>{

   // private ArrayAdapter<Event[]> mAdapter;
    @Override
    protected void onPreExecute(){

       //mAdapter= (ArrayAdapter<Event[]>) listEvent.getAdapter();

    }

    @Override
    protected Void doInBackground(Void... params) {
        //for(Event event:eventList){
        //    publishProgress(event);
        //}
        return null;
    }

    @Override
    protected  void onProgressUpdate(Event... values) {
       // mAdapter.add(values);
    }
    @Override
    protected void onPostExecute(Void result){
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//        View mView = getLayoutInflater(savedInstanceState).inflate(R.layout.popup_event,null);
//        mBuilder.setView(mView);
//        AlertDialog dialog = mBuilder.create();
//        dialog.show();
       // Toast.makeText(mContext )

        Toast.makeText(getActivity().getApplication(),"hoi ik ben async",Toast.LENGTH_LONG).show();

    }
}
 public static void s(Context context, String message){
     Toast.makeText(context, message,Toast.LENGTH_SHORT).show();

 }
}

