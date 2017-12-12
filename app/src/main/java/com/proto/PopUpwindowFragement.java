package com.proto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tyczj.extendedcalendarview.Day;
import com.tyczj.extendedcalendarview.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marloes on 10-12-2017.
 */

public class PopUpwindowFragement extends DialogFragment  {
    public void updateDay(Day day){
        Toast.makeText(getActivity().getApplication(),"ik ben in fragment b",Toast.LENGTH_LONG).show();

    }


}