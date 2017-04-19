package ru.temoteam.artek.app.game;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.Global;
import ru.temoteam.artek.app.RecyclerItemClickListener;
import ru.temoteam.artek.app.adapters.PointsReciclerAdapter;
import ru.temoteam.artek.app.main.ArtekFragment;
import ru.temoteam.artek.app.main.NoInternetFragment;

public class StartGameFragment extends ArtekFragment {

    StartGameFragment it;
    private String name = "List";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PointsReciclerAdapter mAdapter;
    private NoInternetFragment noInternetFragment;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> urls;
    private ArrayList<Boolean> complited;

    private ProgressDialog progressDialog;


    public StartGameFragment() {
        super.init(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }


        return inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_start_game, null);
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

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(ru.temoteam.artek.app.R.id.my_recycler_view);
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

        HashMap<String, String> parms = new HashMap<String, String>();
        parms.put("camp", Global.sharedPreferences.getString(Global.SharedPreferencesTags.CAMP, null));
        parms.put("id",Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null));
        new GetContent().execute(parms);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        it=this;

    }

    class GetContent extends AsyncTask<HashMap<String, String>, String, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Загрузка данных");
            progressDialog.setCancelable(false);
            progressDialog.show();
            titles = new ArrayList<String>();
            descriptions = new ArrayList<String>();
            urls = new ArrayList<String>();
            complited = new ArrayList<Boolean>();
        }

        @Override
        protected Boolean doInBackground(HashMap<String, String>... params) {
            try {
                URL url = new URL(Global.server+"artek/get_achivements.php?camp=" + params[0].get("camp") + "&id=" + params[0].get("id"));
                InputStream input = url.openStream();
                Scanner in = new Scanner(input).useDelimiter("<br />");
                while (in.hasNext()) {
                    String scanString = in.next();
                    Scanner scan = new Scanner(scanString).useDelimiter(";");
                    scan.next();
                    publishProgress(scan.next(), scan.next(), scan.next(), scan.next());


                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            titles.add(values[0]);
            descriptions.add(values[1]);
            urls.add(values[2]);
            complited.add(values[3].equals("true"));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {

                if (noInternetFragment == null) {
                    noInternetFragment = new NoInternetFragment();
                    noInternetFragment.setFrom(it);
                }
                getFragmentManager().beginTransaction().replace(ru.temoteam.artek.app.R.id.frgmCont, noInternetFragment).commit();
            } else {
                mRecyclerView = (RecyclerView) getActivity().findViewById(ru.temoteam.artek.app.R.id.my_recycler_view);

                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new PointsReciclerAdapter(getActivity());
                mAdapter.init(titles, descriptions, urls, complited);
                mRecyclerView.setAdapter(mAdapter);

            }

            progressDialog.hide();
        }
    }

}