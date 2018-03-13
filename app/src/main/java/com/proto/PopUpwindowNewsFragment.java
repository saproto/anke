package com.proto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Marloes on 10-12-2017.
 */

public class PopUpwindowNewsFragment extends DialogFragment  {
    TextView test;

    PopUpwindowNewsFragment(){
    }

    public interface update{
        //test.setText("ik ben in fragement b en ging niet dood!!!!");


        // Toast.makeText(getActivity().getApplication(),"ik ben in fragment b",Toast.LENGTH_LONG).show();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.popup_event, container, false);

        super.onViewCreated(v, savedInstanceState);
        // test = (TextView) v.findViewById(R.id.test);

        return v;
    }
    private update mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
//
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement ");

        }
//
    }

    public void updateDay(Day day){

    }
}
