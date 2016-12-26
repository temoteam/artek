package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerAdapter;

public class DetailSpotFragment extends Fragment {

    private String name = "DetailedSpot";
    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;

    FragmentTransaction fTrans;
    VisitedFragment visitedFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        return inflater.inflate(R.layout.fragment_detail_spot, null);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    getDataSet();
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(getContext().openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {
                content = content + str;
            }

            // close stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    public void getDataSet() {

        //RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view2);
        TextView tw = (TextView) getActivity().findViewById(R.id.tv_recycler_item1);
        TextView tw2 = (TextView) getActivity().findViewById(R.id.tv_recycler_item12);
        ImageView iw = (ImageView)getActivity().findViewById(R.id.tv_recycler_item_img);
        switch (readFile("currcardclick")){
            case "Монумент «Дружба детей мира» ":
                String kek[] = readFile("63").split("#");
                tw.setText(kek[0]);
                iw.setImageResource(R.drawable.monument);
                tw2.setText(kek[1]);
                   // myDataSet.add(readFile("63").replaceAll("#", ""));
                break;
            case "Площадь Дружбы ":
                String kek1[] = readFile("70").split("#");
                tw.setText(kek1[0]);
                iw.setImageResource(R.drawable.ploshad);
                tw2.setText(kek1[1]);
                   // myDataSet.add(readFile("63").replaceAll("#", ""));
                break;

        }


    }


}