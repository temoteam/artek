package ru.temoteam.artek.app.game;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Scanner;

import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.FileRW;
import ru.temoteam.artek.app.Global;
import ru.temoteam.artek.app.R;
import ru.temoteam.artek.app.RecyclerItemClickListener;
import ru.temoteam.artek.app.account.AccountManager;
import ru.temoteam.artek.app.adapters.RecyclerAdapter;
import ru.temoteam.artek.app.main.ArtekFragment;
import tyrantgit.explosionfield.ExplosionField;

public class VisitedFragment extends ArtekFragment implements AccountManager.ReciclerInterface {

    private String name = "Visited";
    private RecyclerView mRecyclerView;
    private FileRW fileRW;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    private ExplosionField explosionField;

    private ArrayList<String> qrs;

    public VisitedFragment() {
        super.init(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }

        title = getString(R.string.scanned_points);
        return inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_visited, null);

    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), getString(ru.temoteam.artek.app.R.string.click_to_send), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Global.accountManager.setReciclerInterface(this);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        fileRW = new FileRW(getActivity());

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(ru.temoteam.artek.app.R.id.my_recycler_view);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        TextView tw = (TextView) view.findViewById(ru.temoteam.artek.app.R.id.tv_recycler_item);
                        String yop = tw.getText().toString();
                        Global.accountManager.sendQR(yop,position,view);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                    }
                })
        );

        qrs = getDataSet();

        mRecyclerView = (RecyclerView) getActivity().findViewById(ru.temoteam.artek.app.R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecyclerAdapter(qrs);
        mRecyclerView.setAdapter(mAdapter);

        explosionField = ExplosionField.attach2Window(getActivity());


    }

    public ArrayList<String> getDataSet() {

        ArrayList<String> myDataSet = new ArrayList<>();
        Scanner in = new Scanner(new FileRW(getActivity()).readFile(Global.SAVED)).useDelimiter(",");
        while (in.hasNext())
            myDataSet.add(in.next());

        return myDataSet;
    }

    @Override
    public void remove(int id,String qr,View view) {

        String saved = fileRW.readFile(Global.SAVED);
        if (saved.indexOf(qr) == 0)
            saved = saved.replace(qr + ",", "");
        else
            saved = saved.replace("," + qr, "");
        fileRW.writeFile(Global.SAVED, saved);
        qrs.remove(id);
        explosionField.explode(view);

        new Remove().execute(id);
    }

    public void add(String qr) {

        qrs.add(qr);

        mAdapter.notifyItemRangeChanged(0, qrs.size());
        mRecyclerView.refreshDrawableState();

        String saved = fileRW.readFile(Global.SAVED);
        if (saved.equals("")) fileRW.writeFile(Global.SAVED, qr);
        else fileRW.writeFile(Global.SAVED, saved + "," + qr);
    }

    class Remove extends AsyncTask<Integer,Void,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyItemRemoved(aVoid);
            mAdapter.notifyItemRangeChanged(0, qrs.size());
            mRecyclerView.refreshDrawableState();
        }
    }


}