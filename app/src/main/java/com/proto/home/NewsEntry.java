package com.proto.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.proto.R;

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
public class NewsEntry {
    public int id;
    public String title;
    public String featured_image_url;
    public String content;
    public Date published_at;


    public NewsEntry(int id, String title, String featured_image_url, String content, long published_at) {
        this.id = id;
        this.title = title;
        this.featured_image_url = featured_image_url;
        this.content = content;
        this.published_at = new Date(published_at*1000);
    }

    /**
     * This is used for knowing the order at which to display events, this one selects thFe
     * start date.
     * TODO: Create comparators for other parameters, such as alphabetical order.
     */
    public static final Comparator<NewsEntry> DATE_COMPARATOR = new Comparator<NewsEntry>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(NewsEntry o1, NewsEntry o2) {
            if (o1.published_at.before(o2.published_at)) {
                return 1;
            } else if (o1.published_at.after(o2.published_at)) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * A custom loader that loads all calendar activities from the proto server.
     */
    public static class NewsListLoader extends AsyncTaskLoader<List<NewsEntry>> {

        String urlToLoad;
        List<NewsEntry> newsEntries;

        public NewsListLoader(Context context, String urlToLoad) {
            super(context);
            this.urlToLoad = urlToLoad;
            Log.d("in News list loader","in News list loader ");
        }

        /**
         * This is where the bulk of the work is done. This function is called in a
         * background thread and should generate a new list of data to be published
         * by the loader.
         * @return The list of calendar entries.
         */
        @Override
        public List<NewsEntry> loadInBackground() {
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
                    newsEntries = new ArrayList<NewsEntry>(events.length());
                    for (int eventIndex = 0; eventIndex < events.length(); eventIndex++) {
                        JSONObject event = events.getJSONObject(eventIndex);
                        NewsEntry newsEntry = new NewsEntry(
                                event.getInt("id"),
                                event.getString("title"),
                                event.getString("featured_image_url"),
                                event.getString("content"),
                                event.getLong("published_at")
                        );
                        newsEntries.add(newsEntry);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                newsEntries = new ArrayList<NewsEntry>(0);
            }

            // Sort the list
            Collections.sort(newsEntries, DATE_COMPARATOR);

            return newsEntries;
        }
    }

    public static class NewsListAdapter extends ArrayAdapter<NewsEntry> {
        private final LayoutInflater layoutInflater;

        public NewsListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            layoutInflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<NewsEntry> data) {
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
                view = layoutInflater.inflate(R.layout.news_item, parent, false);
            } else {
                view = convertView;
            }

            // TODO: Look into this to make this format better and use the other fields
            NewsEntry entry = getItem(position);
            ((TextView)view.findViewById(R.id.news_title)).setText(entry.title);
            ((TextView)view.findViewById(R.id.news_content)).setText(entry.content);
            ((TextView)view.findViewById(R.id.news_published_at)).setText(entry.published_at.toString());

            return view;
        }
    }
}


