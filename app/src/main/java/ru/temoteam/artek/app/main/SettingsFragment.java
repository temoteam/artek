package ru.temoteam.artek.app.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.Global;

public class SettingsFragment extends ArtekFragment {
    private String name = "Settings";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        return inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_settings, null);

    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        super.init(false);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        Button button = (Button) getActivity().findViewById(ru.temoteam.artek.app.R.id.resetapp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.sharedPreferences.edit().clear().apply();
                getActivity().finish();
            }
        });



    }


}