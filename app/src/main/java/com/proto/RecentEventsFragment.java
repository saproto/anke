package com.proto;

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

public class RecentEventsFragment
        extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<CalendarEntry>> {

    // Adapter used to display the list's data.
    CalendarEntry.CalendarListAdapter adapter;
    // The search view for filtering
    View listContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recent_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // URL: "https://www.proto.utwente.nl/api/events/upcoming"

        listContainer = getListView();

        // Create a progress bar.
        ProgressBar progressBar = new ProgressBar(view.getContext());
        progressBar.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Create new cursor adapter which we will fill with the display data
        adapter = new CalendarEntry.CalendarListAdapter(getActivity());
        setListAdapter(adapter);

        // Start out with a progress indicator
        //setListShown(false);

        // Prepare the loader
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    /**
     * Gets called when a new loader needs to be created.
     * @param id
     * @param args
     * @return a new loader for calendar entries.
     */
    @Override public Loader<List<CalendarEntry>> onCreateLoader(int id, Bundle args) {
        return new CalendarEntry.CalendarListLoader(getActivity(), "https://www.proto.utwente.nl/api/events/upcoming");
    }

    @Override public void onLoadFinished(Loader<List<CalendarEntry>> loader,
                                         List<CalendarEntry> entries) {
        adapter.setData(entries);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override public void onLoaderReset(Loader<List<CalendarEntry>> loader) {
        adapter.setData(null);
    }

    boolean isListShown = false;
    public void setListShown(boolean shown, boolean animate) {
        if (isListShown == shown) {
            return;
        }
        isListShown = shown;
        if (shown) {
            if (animate) {
                listContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            }
            listContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                listContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            }
        }
    }

    @Override public void setListShown(boolean shown){
        setListShown(shown, true);
    }
    @Override public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }
}
