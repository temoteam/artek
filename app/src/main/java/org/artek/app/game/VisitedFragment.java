package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.FileRW;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerAdapter;
import org.artek.app.RecyclerItemClickListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

public class VisitedFragment extends Fragment {

    private String name = "Visited";
    private RecyclerView mRecyclerView;
    private FileRW fileRW;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    FragmentTransaction fTrans;
    DetailSpotFragment detailSpotFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }

        return inflater.inflate(R.layout.fragment_visited, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(),"Для нажмите для отправки кода на сервер",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        fileRW = new FileRW(getActivity());

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView tw = (TextView)view.findViewById(R.id.tv_recycler_item);
                        String yop = tw.getText().toString();
                        Global.accountManager.sendQR(yop);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );
        ArrayList<String> myDataset = getDataSet();

        //HashMap<Integer, String> myDataset= new Map<Integer, String>();

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


    }

    public ArrayList<String> getDataSet() {

        ArrayList<String> myDataSet = new ArrayList<>();
        Scanner in = new Scanner(new FileRW(getActivity()).readFile(GameActivity.SAVED)).useDelimiter(",");
        while (in.hasNext())
            myDataSet.add(in.next());

        return myDataSet;
    }

}