package com.proto;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * This class holds the data for a calendar entry.
 */
public class ArticleEntry {
    //public int id;
    public String title;
    public String description;
    public String link;
    public Date date;
    public String thumbnail;
    //public Drawable thumbnail;

    public ArticleEntry(String title, String description, String link, long date, String thumbnail) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = new Date(date*1000);
        this.thumbnail = thumbnail;
        //this.thumbnail= thumbnail;
    }

    /**
     * This is used for knowing the order at which to display events, this one selects thFe
     * start date.
     * TODO: Create comparators for other parameters, such as alphabetical order.
     */
    public static final Comparator<ArticleEntry> DATE_COMPARATOR = new Comparator<ArticleEntry>() {
        private final Collator collator = Collator.getInstance();
        @Override
        public int compare(ArticleEntry o1, ArticleEntry o2) {
            if (o1.date.before(o2.date)) {
                return 1;
            } else if (o1.date.after(o2.date)) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    /**
     * A custom loader that loads all calendar activities from the proto server.
     */
    public static class ArticleListLoader extends AsyncTaskLoader<List<ArticleEntry>> {

        String urlToLoad;
        List<ArticleEntry> articleEntries;

        public ArticleListLoader(Context context, String urlToLoad) {
            super(context);
            this.urlToLoad = urlToLoad;
            Log.d("in Article list loader","in aritcle list loader ");
        }

        /**
         * This is where the bulk of the work is done. This function is called in a
         * background thread and should generate a new list of data to be published
         * by the loader.
         * @return The list of calendar entries.
         */
        @Override
        public List<ArticleEntry> loadInBackground() {
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
                    articleEntries = new ArrayList<ArticleEntry>(events.length());
                    for (int eventIndex = 0; eventIndex < events.length(); eventIndex++) {
                        JSONObject event = events.getJSONObject(eventIndex);
                        ArticleEntry articleEntry = new ArticleEntry(
                              //  event.getInt("id"),
                                event.getString("title"),
                                event.getString("description"),
                                event.getString("link"),
                                event.getLong("date"),
                                event.getString("thumbnail")
                        );
                        articleEntries.add(articleEntry);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                articleEntries = new ArrayList<ArticleEntry>(0);
            }

            // Sort the list
            Collections.sort(articleEntries, DATE_COMPARATOR);

            return articleEntries;
        }
    }

    public static class ArticleListAdapter extends ArrayAdapter<ArticleEntry> {
        private final LayoutInflater layoutInflater;

        public ArticleListAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
            layoutInflater = (LayoutInflater)context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
        }

        public void setData(List<ArticleEntry> data) {
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
            //Bitmap bitmapThumbnail;

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.article_item, parent, false);
            } else {
                view = convertView;
            }

            ArticleEntry entry = getItem(position);

            Bitmap bitmapThumbnail= getbmpfromURL(entry.thumbnail);
            Drawable imageThumbnail = new BitmapDrawable(getContext().getResources(), bitmapThumbnail);

            // TODO: Look into this to make this format better and use the other fields

            ((TextView)view.findViewById(R.id.article_title)).setText(entry.title);
            ((TextView)view.findViewById(R.id.article_description)).setText(entry.description);
            ((TextView)view.findViewById(R.id.article_date)).setText(entry.date.toString());

            ((ImageView)view.findViewById(R.id.article_thumbnail)).setImageBitmap(bitmapThumbnail);

            return view;
        }

        public Bitmap getbmpfromURL(String surl){
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                URL url = new URL(surl);
                HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
                urlcon.setDoInput(true);
                urlcon.setDoOutput(true);
                urlcon.connect();

                int response = urlcon.getResponseCode();
                InputStream in = urlcon.getInputStream();
                Bitmap mIcon = BitmapFactory.decodeStream(in);
                return  mIcon;
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }

        }
    }
}

