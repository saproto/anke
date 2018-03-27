package com.proto.calendar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import java.util.List;

public class DayPopUpListFragment{

        public String title;
        public String startDate;
        public String endDate;
        public String location;
        public String description;

        public DayPopUpListFragment(String title, String startDate, String endDate, String location, String description) {
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
            this.location = location;
            this.description = description;
        }
    }
