package com.proto;

import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class PopUpWindowActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_window);
        setTitle(getIntent().getStringExtra("day"));

        int numEvent = getIntent().getIntExtra("numEvent",0);

        ArrayList<String> titleArray = getIntent().getStringArrayListExtra("title");
        ArrayList<String>  startArray = getIntent().getStringArrayListExtra("startDate");
        ArrayList<String>  endArray = getIntent().getStringArrayListExtra("endDate");
        ArrayList<String>  locationArray = getIntent().getStringArrayListExtra("location");
        ArrayList<String>  descriptionArray = getIntent().getStringArrayListExtra("description");

        ArrayList<DayPopUpListFragment> events = new ArrayList<DayPopUpListFragment>();

        for (int i =0; i<numEvent; i ++){
            String title = titleArray.get(i);
            String startDate = startArray.get(i);
            String endDate = endArray.get(i);
            String location = locationArray.get(i);
            String description = descriptionArray.get(i);

            DayPopUpListFragment hoi = new DayPopUpListFragment(title,startDate, endDate, location,description);
            events.add(hoi);

        }


        ListView listView = (ListView) findViewById(R.id.ListView);

        ArrayList<DayPopUpListFragment> subjects = new ArrayList<DayPopUpListFragment>();

        CalenderViewAdapter adapter = new CalenderViewAdapter(getApplicationContext(), R.layout.row_popup_event, events);

        listView.setAdapter(adapter);
    }
}
