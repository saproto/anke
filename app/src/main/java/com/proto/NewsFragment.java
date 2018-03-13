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

public class NewsFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<NewsEntry>>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    NewsEntry.NewsListAdapter adapter;

    View listContainer;

    //SwipeRefreshLayout refreshLayout;

    public NewsFragment() {
        // Required empty public constructor
    }

    public interface OnDaySelectedListener {
        void onDaySelected(Day day);
    }

    private OnDaySelectedListener sDummyCallbacks = new OnDaySelectedListener() {
        @Override
        public void onDaySelected(Day day) {
        }
    };

    private OnDaySelectedListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

//          if (context instanceof OnDaySelectedListener) {
            mListener = (OnDaySelectedListener) context;
            //    }
        } catch (ClassCastException e) {

            throw new ClassCastException(context.toString() + " must implement OnDaySelectedListener");

        }
//
    }

  /* public static InkFragment newInstance(String param1, String param2){
       InkFragment fragment = new InkFragment ();
       return fragment;
   }*/

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_news);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return inflater.inflate(R.layout.fragment_news, container, false);
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
        adapter = new NewsEntry.NewsListAdapter(getActivity());
        setListAdapter(adapter);

        // Start out with a progress indicator
        //setListShown(false);

        // Prepare the loader
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    /*@Override public Loader<List<ArticleEntry>> onCreateLoader(int id, Bundle args) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https")
                .authority("peterv.dev.saproto.nl")
                .appendPath("api")
                .appendPath("protoi")
                .appendQueryParameter("username", "yolouser")
                .appendQueryParameter("password", "yoloswak")
                .appendQueryParameter("start", Long.toString(1323190800))
                .appendQueryParameter("count", Integer.toString(5));
        return new ArticleEntry.ArticleListLoader(getActivity(), (uri.build()).toString());
    }*/

    @Override public Loader<List<NewsEntry>> onCreateLoader(int id, Bundle args) {
        return new NewsEntry.NewsListLoader(getActivity(), "https://www.proto.utwente.nl/api/news");
    }

    @Override public void onLoadFinished(Loader<List<NewsEntry>> loader,
                                         List<NewsEntry> entries) {
        adapter.setData(entries);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override public void onLoaderReset(Loader<List<NewsEntry>> loader) {
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