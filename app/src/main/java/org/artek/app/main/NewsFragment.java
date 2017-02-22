package org.artek.app.main;


import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.adapters.NewsRecyclerAdapter;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsFragment extends Fragment {


    private final OkHttpClient client = new OkHttpClient();
    String owner_id = "-44235988";
    private boolean lol = true;
    RecyclerView rw;
    Context baseContext;
    NoInternetFragment noInternetFragment;
    private String name = "News";


    public NewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        setRetainInstance(true);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

    @Override
    public void onResume() {
        super.onResume();
        baseContext = getActivity().getBaseContext();
        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setFrom(this);
        String strUrl = "https://api.vk.com/method/wall.get?owner_id=" + owner_id + "&count=100";
        downloadTask(strUrl);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_news, container, false);
        rw = (RecyclerView) result.findViewById(R.id.recycler);
        rw.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                                                getFragmentManager().beginTransaction().replace(R.id.frgmCont,noInternetFragment).commit();
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
            NewsRecyclerAdapter nra = new NewsRecyclerAdapter(hashMaps,getActivity());
            Log.i("Parser","post "+hashMaps.size());
            rw.setAdapter(nra);
        }
    }


}


