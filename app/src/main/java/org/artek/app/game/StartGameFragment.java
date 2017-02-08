package org.artek.app.game;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.RecyclerItemClickListener;
import org.artek.app.adapters.PointsReciclerAdapter;
import org.artek.app.main.NoInternetFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StartGameFragment extends Fragment {

    private final OkHttpClient client = new OkHttpClient();
    StartGameFragment it;
    private String name = "List";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PointsReciclerAdapter mAdapter;
    private NoInternetFragment noInternetFragment;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> urls;
    private ArrayList<Boolean> complited;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }

        return inflater.inflate(R.layout.fragment_start_game, null);
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Bundle data = new Bundle();
                        data.putString("title",titles.get(position));
                        data.putString("description",descriptions.get(position));
                        data.putString("url",urls.get(position));

                        Intent intent = new Intent(getActivity(),PointActivity.class);
                        intent.putExtras(data);
                        getActivity().startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        HashMap<String, String> parms = new HashMap<>();
        parms.put("camp",Global.sharedPreferences.getString(Global.SharedPreferencesTags.CAMP,null));
        parms.put("id",Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null));
        getData(parms);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        it=this;

    }


    private void getData(HashMap<String, String> params) {

        final Request request = new Request.Builder()
                .url(getString(R.string.main_domain) + "/artek/get_achivements.php?camp=" + params.get("camp") + "&id=" + params.get("id"))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Загрузка данных");
        progressDialog.setCancelable(false);
        progressDialog.show();
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        urls = new ArrayList<>();
        complited = new ArrayList<>();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (noInternetFragment == null) {
                    noInternetFragment = new NoInternetFragment();
                    noInternetFragment.setFrom(it);
                }
                progressDialog.cancel();
                getFragmentManager().beginTransaction().replace(R.id.frgmCont, noInternetFragment).commit();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseString = response.body().string();
                String[] points = responseString.split("<br />");

                for (int i = 1; i < points.length; ++i) {
                    String[] point = points[i].split(";");


                    titles.add(point[0]);
                    descriptions.add(point[1]);
                    urls.add(point[2]);
                    complited.add(point[3].equals("true"));
                }


                mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
                Log.d("po", "1");

                mRecyclerView.setHasFixedSize(true);

                Log.d("po", "2");
                mLayoutManager = new LinearLayoutManager(getContext());

                Log.d("po", "3");
                mRecyclerView.setLayoutManager(mLayoutManager);

                Log.d("po", "4");

                mAdapter = new PointsReciclerAdapter(getActivity());

                Log.d("po", "5");
                mAdapter.init(titles, descriptions, urls, complited);

                Log.d("po", "6");
                mRecyclerView.setAdapter(mAdapter);

                Log.d("po", "7");
                progressDialog.cancel();

                Log.d("po", "8");


            }
            }

        );
    }

}