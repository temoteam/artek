package ru.temoteam.artek.app.main;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.R;
import ru.temoteam.artek.app.adapters.NewsRecyclerAdapter;


public class NewsFragment extends ArtekFragment {


    private final OkHttpClient client = new OkHttpClient();
    private String owner_id = "-44235988";
    private boolean lol = true;
    private RecyclerView rw;
    private NoInternetFragment noInternetFragment;
    private String name = "News";
    private RecyclerView.LayoutManager layoutManager;
    private NewsRecyclerAdapter newsRecyclerAdapter = null;


    public NewsFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(false);
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        setRetainInstance(true);
        title = getString(R.string.news);
        layoutManager = new LinearLayoutManager(getActivity());
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        //
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("news","resumed");

        if (rw.getLayoutManager()==null)
            rw.setLayoutManager(new LinearLayoutManager(getActivity()));


        if (newsRecyclerAdapter==null)
             downloadTask("https://api.vk.com/method/wall.get?owner_id=" + owner_id + "&count=100");
        else
            rw.setAdapter(newsRecyclerAdapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_news, container, false);
        rw = (RecyclerView) result.findViewById(ru.temoteam.artek.app.R.id.recycler);
        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setFrom(this);

        return result;
    }



    private void downloadTask(String uri) {
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                             e.printStackTrace();
                                                getFragmentManager().beginTransaction().replace(ru.temoteam.artek.app.R.id.frgmCont, noInternetFragment).commit();
                                             }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String message = response.body().string();
                                                if(lol){
                                                    lol = false;
                                                    ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
                                                    listViewLoaderTask.execute(message);}

                                            }
                                        }

        );
    }



    private class ListViewLoaderTask extends AsyncTask<String, Void, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strJson) {
            try {
                jObject = new JSONObject(strJson[0]);
                NewsJSONParser newsJsonParser = new NewsJSONParser();
            } catch (Exception e) {
                e.printStackTrace();
            }

            NewsJSONParser newsJsonParser = new NewsJSONParser();
            List<HashMap<String, String>> countries = null;
            try {
                countries = newsJsonParser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return countries;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            super.onPostExecute(hashMaps);
            newsRecyclerAdapter = new NewsRecyclerAdapter(hashMaps,getActivity());
            //rw.setLayoutManager(layoutManager);
            rw.setAdapter(newsRecyclerAdapter);
            Log.i("downloadtask","finished");
        }
    }


}


