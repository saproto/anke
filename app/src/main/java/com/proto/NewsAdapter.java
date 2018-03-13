package com.proto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;


/**
 * Created by Marloes on 18-2-2018.
 */

public class NewsAdapter extends ArrayAdapter<DayPopUpListFragment> {
    Context context;
    int resource;

    ArrayList<DayPopUpListFragment> subjects = null;
    public NewsAdapter(Context context, int resource, ArrayList<DayPopUpListFragment> subjects){
        super(context, resource, subjects);
        this.context = context;
        this.resource = resource;
        this.subjects = subjects;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayPopUpListFragment subject = subjects.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_popup_event, parent, false);
        }

        TextView titleTextView = (TextView)convertView.findViewById(R.id.txvTitle);
        TextView startDateTextView = (TextView) convertView.findViewById(R.id.txvStartDate);
        TextView endDateTextView = (TextView) convertView.findViewById(R.id.txvEndDate);
        TextView locationTextView = (TextView) convertView.findViewById(R.id.txvLocation);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.txvDescription);

        titleTextView.setText(subject.title);
        startDateTextView.setText(subject.startDate);
        endDateTextView.setText(subject.endDate);
        locationTextView.setText(subject.location);
        descriptionTextView.setText(subject.description);

        return convertView;
    }
}
