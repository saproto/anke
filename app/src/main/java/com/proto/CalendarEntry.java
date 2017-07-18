package com.proto;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

/**
 * This class holds the data for a calendar entry.
 */
public class CalendarEntry {
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

        String urlToLoad;
        List<CalendarEntry> calendarEntries;

        public CalendarListLoader(Context context, String urlToLoad) {
            super(context);
            this.urlToLoad = urlToLoad;
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
                URL url = new URL(urlToLoad);
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
}
