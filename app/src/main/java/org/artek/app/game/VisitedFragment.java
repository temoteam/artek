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
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.FileRW;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.RecyclerItemClickListener;
import org.artek.app.account.AccountManager;
import org.artek.app.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.Scanner;

public class VisitedFragment extends Fragment implements AccountManager.ReciclerInterface {

    private String name = "Visited";
    private RecyclerView mRecyclerView;
    private FileRW fileRW;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private ArrayList<String> qrs;

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
        Toast.makeText(getActivity(), getString(R.string.click_to_send), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Global.accountManager.setReciclerInterface((AccountManager.ReciclerInterface) this);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        fileRW = new FileRW(getActivity());

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView tw = (TextView)view.findViewById(R.id.tv_recycler_item);
                        String yop = tw.getText().toString();
                        Global.accountManager.sendQR(yop,position);
                        Log.i("Clicked Position",position+"");
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        qrs = getDataSet();



        //HashMap<Integer, String> myDataset= new Map<Integer, String>();

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(qrs);
        mRecyclerView.setAdapter(mAdapter);


    }

    public ArrayList<String> getDataSet() {

        ArrayList<String> myDataSet = new ArrayList<>();
        Scanner in = new Scanner(new FileRW(getActivity()).readFile(GameActivity.SAVED)).useDelimiter(",");
        while (in.hasNext())
            myDataSet.add(in.next());

        return myDataSet;
    }

    @Override
    public void remove(int id,String qr) {
        Log.i("removing",""+id);
            String saved = fileRW.readFile(GameActivity.SAVED);
            if (saved.indexOf(qr) == 0)
                saved = saved.replace(qr + ",", "");
            else
                saved = saved.replace("," + qr, "");
            fileRW.writeFile(GameActivity.SAVED, saved);
        qrs.remove(id);
        mAdapter.notifyItemRemoved(id);
        mAdapter.notifyItemRangeChanged(0, qrs.size());
        mRecyclerView.refreshDrawableState();
    }


    public void addAt(String qr) {

        qrs.add(qr);

        mAdapter.notifyItemRangeChanged(0, qrs.size());
        mRecyclerView.refreshDrawableState();

        String saved = fileRW.readFile(GameActivity.SAVED);
        if (saved.equals("")) fileRW.writeFile(GameActivity.SAVED,qr);
        else fileRW.writeFile(GameActivity.SAVED,saved+","+qr);
    }
}