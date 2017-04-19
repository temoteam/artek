package ru.temoteam.artek.app.game;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.main.ArtekFragment;

public class DetailSpotFragment extends ArtekFragment {

    private String name = "DetailedSpot";

    public DetailSpotFragment(){
        super.init(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        return inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_detail_spot, null);

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
    public void getDataSet() {

    }


}