package com.proto;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.*;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;

public class CalendarFragment extends ListFragment
        implements OnCloseListener, OnQueryTextListener,
        LoaderManager.LoaderCallbacks<List<CalendarFragment.CalendarEntry>> {

    /**
     * This class holds the data for a calendar entry.
     */
    public static class CalendarEntry {
        public int id;
        public String title;
        public String description;

        public Date startDate;
        public Date endDate;

        public String location;
        public boolean current;
        public boolean over;

        public CalendarEntry(int id, String title, String description, long start, long end,
                             String location, boolean current, boolean over) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.location = location;

            this.startDate = new Date(start * 1000);
            this.endDate = new Date(end * 1000);

            this.current = current;
            this.over = over;
        }
    }

    /**
     * This is used for knowing the order at which to display events, this one selects the
     * start date.
     * TODO: Create comparators for other parameters, such as alphabetical order.
     */
    public static final Comparator<CalendarEntry> DATE_COMPARATOR = new Comparator<CalendarEntry>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(CalendarEntry o1, CalendarEntry o2) {
            if (o1.startDate.before(o2.startDate)) {
                return -1;
            } else if (o1.startDate.after(o2.startDate)) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    /**
     * A custom loader that loads all calendar activities from the proto server.
     */
    public static class CalendarListLoader extends AsyncTaskLoader<List<CalendarEntry>> {

        List<CalendarEntry> calendarEntries;

        public CalendarListLoader(Context context) {
            super(context);
        }

        /**
         * This is where the bulk of the work is done. This function is called in a
         * background thread and should generate a new list of data to be published
         * by the loader.
         * @return The list of calendar entries.
         */
        @Override
        public List<CalendarEntry> loadInBackground() {
            String response = null;
            // Open connection and catch result.
            try {
                URL url = new URL("https://www.proto.utwente.nl/api/events/upcoming");
                HttpURLConnection url_connection = null;
                StringBuffer stringBuffer = new StringBuffer();
                InputStream input = null;

                try {
                    url_connection = (HttpURLConnection)url.openConnection();
                    input = new BufferedInputStream(url_connection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
                    String inputLine = "";
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        stringBuffer.append(inputLine);
                    }
                    response = stringBuffer.toString();
                } catch (Exception e) {
                    System.out.print(url_connection.getErrorStream());
                    System.out.print(e.getMessage());
                } finally {
                    url_connection.disconnect();
                }
            } catch (MalformedURLException e) {
                // Our url is not malformed.
            }

            // Create calendar entries based on the amount of
            if (response != null) {
                try {
                    JSONArray events = new JSONArray(response);
                    calendarEntries = new ArrayList<CalendarEntry>(events.length());
                    for (int eventIndex = 0; eventIndex < events.length(); eventIndex++) {
                        JSONObject event = events.getJSONObject(eventIndex);
                        CalendarEntry calendarEntry = new CalendarEntry(
                                event.getInt("id"),
                                event.getString("title"),
                                event.getString("description"),
                                event.getLong("start"),
                                event.getLong("end"),
                                event.getString("location"),
                                event.getBoolean("current"),
                                event.getBoolean("over")
                        );
                        calendarEntries.add(calendarEntry);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                calendarEntries = new ArrayList<CalendarEntry>(0);
            }

            // Sort the list
            Collections.sort(calendarEntries, DATE_COMPARATOR);

            return calendarEntries;
        }
    }

    public static class CalendarListAdapter extends ArrayAdapter<CalendarEntry> {
        private final LayoutInflater layoutInflater;

        public CalendarListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            layoutInflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<CalendarEntry> data) {
            clear();
            if (data != null) {
                addAll(data);
            }
        }

        /**
         * Populate new data in the list
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view;

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.calendar_item, parent, false);
            } else {
                view = convertView;
            }

            // TODO: Look into this to make this format better and use the other fields
            CalendarEntry entry = getItem(position);
            ((TextView)view.findViewById(R.id.calendar_title)).setText(entry.title);
            ((TextView)view.findViewById(R.id.calendar_location)).setText(entry.location);
            ((TextView)view.findViewById(R.id.calendar_start_date)).setText(entry.startDate.toString());
            ((TextView)view.findViewById(R.id.calendar_end_date)).setText(entry.endDate.toString());

            return view;
        }
    }

    // Adapter used to display the list's data.
    CalendarListAdapter adapter;
    // The search view for filtering
    SearchView searchView;
    View listContainer;
    // If non-null this is the current filter the user has provided
    String currentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, root);

        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Menu 1");

        listContainer = getListView();

        // Create a progress bar.
        ProgressBar progressBar = new ProgressBar(this.getContext());
        progressBar.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Add progress bar to the root layout
        ViewGroup root = (ViewGroup)view.getRootView();
        root.addView(progressBar);


        // Create new cursor adapter which we will fill with the display data
        adapter = new CalendarListAdapter(getActivity());
        setListAdapter(adapter);

        // Start out with a progress indicator
        //setListShown(false);

        // Prepare the loader
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    public static class CalendarSearchView extends SearchView {
        public CalendarSearchView(Context context) {
            super(context);
        }

        // The normal SearchView doesn't clear its search text when collapsed.
        public void onActionViewCollapsed() {
            setQuery("", false);
            super.onActionViewCollapsed();
        }
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                            MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        searchView = new CalendarSearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.setIconifiedByDefault(true);
        item.setActionView(searchView);
    }

    /**
     * Change the filter based on the text in the search bar.
     * @param query
     * @return true;
     */
    @Override public boolean onQueryTextChange(String query) {
        // Called when the action bar search text has changed.
        currentFilter = !TextUtils.isEmpty(query) ? query : null;
        adapter.getFilter().filter(currentFilter);
        return true;
    }

    /**
     * Don't care.
     * @param query Not used.
     * @return true
     */
    @Override public boolean onQueryTextSubmit(String query) {
        return true;
    }

    /**
     * Clear the query when we close searching. View all elements.
     * @return true
     */
    @Override public boolean onClose() {
        if (!TextUtils.isEmpty(searchView.getQuery())) {
            searchView.setQuery(null, true);
        }
        return true;
    }

    @Override public void onListItemClick(ListView listView, View view, int position, long id) {
        // TODO: Implement detail view on click.
    }

    /**
     * Gets called when a new loader needs to be created.
     * @param id
     * @param args
     * @return a new loader for calendar entries.
     */
    @Override public Loader<List<CalendarEntry>> onCreateLoader(int id, Bundle args) {
        return new CalendarListLoader(getActivity());
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