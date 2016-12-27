package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;

public class SettingsFragment extends Fragment {
    private String name = "Settings";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        return inflater.inflate(R.layout.fragment_settings, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Button button = (Button) getActivity().findViewById(R.id.resetapp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.initilizate(getActivity());
                Global.sharedPreferences.edit().clear().commit();
                getActivity().finish();
            }
        });



    }


}