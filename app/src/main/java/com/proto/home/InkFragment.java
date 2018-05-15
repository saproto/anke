package com.proto.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.proto.R;
import com.proto.home.ArticleEntry;
import com.proto.oauth.SAProtoClient;
import com.proto.service.RetroFitServiceGenerator;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.Arrays.asList;

public class InkFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<ArticleEntry>>{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ArticleEntry.ArticleListAdapter adapter;

    View listContainer;

    SAProtoClient service;

    //SwipeRefreshLayout refreshLayout;

    public InkFragment() {
        // Required empty public constructor
    }


  /* public static InkFragment newInstance(String param1, String param2){
       InkFragment fragment = new InkFragment ();
       return fragment;
   }*/

   @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       //setContentView(R.layout.fragment_ink);

       service = RetroFitServiceGenerator.createService(SAProtoClient.class);


   }

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

       return inflater.inflate(R.layout.fragment_ink, container, false);
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
        adapter = new ArticleEntry.ArticleListAdapter(getActivity());
        setListAdapter(adapter);

        // Start out with a progress indicator
        //setListShown(false);

        // Prepare the loader
        //getLoaderManager().initLoader(0, null, this).forceLoad();
        getArticles();
    }

    /*@Override public Loader<List<ArticleEntry>> onCreateLoader(int id, Bundle args) {
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("https")
                .authority("peterv.dev.saproto.nl")
                .appendPath("api")
                .appendPath("protoink")
                .appendQueryParameter("username", "yolouser")
                .appendQueryParameter("password", "yoloswak")
                .appendQueryParameter("start", Long.toString(1323190800))
                .appendQueryParameter("count", Integer.toString(5));
        return new ArticleEntry.ArticleListLoader(getActivity(), (uri.build()).toString());
    }*/

    @Override public Loader<List<ArticleEntry>> onCreateLoader(int id, Bundle args) {
        return new ArticleEntry.ArticleListLoader(getActivity(), "https://www.proto.utwente.nl/api/protoink");
    }



    @Override public void onLoadFinished(Loader<List<ArticleEntry>> loader,
                                         List<ArticleEntry> entries) {
        adapter.setData(entries);

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override public void onLoaderReset(Loader<List<ArticleEntry>> loader) {
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

    public void getArticles(){

        Call<ArticleEntry[]> articles = service.getProtoInkArticles();

        articles.enqueue(new Callback<ArticleEntry[]>() {
            @Override
            public void onResponse(Call<ArticleEntry[]> call, Response<ArticleEntry[]> response) {
                ArticleEntry[] res = response.body();

                List<ArticleEntry> entries = Arrays.asList(response.body());
                adapter.setData(entries);

                    if (isResumed()) {
                        setListShown(true);
                    } else {
                        setListShownNoAnimation(true);
                    }
                //Toast.makeText(HomeFragment.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ArticleEntry[]> call, Throwable t) {
                Log.d("InkFragment","Getting the articles was a faillure");
            }
        });
    }

}


