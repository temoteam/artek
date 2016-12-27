package org.artek.app.game;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.RecyclerItemClickListener;
import org.artek.app.adapters.PointsReciclerAdapter;
import org.artek.app.adapters.RecyclerAdapter;

import java.util.ArrayList;

public class StartGameFragment extends Fragment {

    private String name = "List";
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PointsReciclerAdapter mAdapter;

    private ArrayList<String> qrs;

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
                        TextView tw = (TextView)view.findViewById(R.id.tv_recycler_item);
                        String yop = tw.getText().toString();
                        //Global.accountManager.sendQR(yop,position);
                        Log.i("Clicked Position",position+"");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        qrs = getDataSet();



        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new PointsReciclerAdapter(qrs);
        mRecyclerView.setAdapter(mAdapter);


    }

    public ArrayList<String> getDataSet() {

        ArrayList<String> myDataSet = new ArrayList<>();
        myDataSet.add("");

        return myDataSet;
    }

}