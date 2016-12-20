package org.artek.app.main;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.RadioService;
import static org.artek.app.R.id.startStopRadio;

public class RadioFragment extends Fragment {

    boolean isPlaying = false;
    BroadcastReceiver service;
    ImageView radioButton;
    private View.OnClickListener pausePlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isPlaying) {
                getActivity().getApplicationContext().stopService(new Intent(getActivity(), RadioService.class));
                isPlaying = false;
                radioButton.setImageResource(R.drawable.mute);
                Log.d("radio", "sendStop");
            } else {
                //if(service!= null){getContext().unregisterReceiver(service);}
                getActivity().getApplicationContext().startService(new Intent(getActivity(), RadioService.class));
                isPlaying = true;
                radioButton.setImageResource(R.drawable.speaker);
                Log.d("radio","sendStart");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio, null);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        radioButton = (ImageView) getActivity().findViewById(startStopRadio);
        radioButton.setOnClickListener(pausePlay);
        //Регистрация приемника
        IntentFilter filter = new IntentFilter();
        filter.addAction("AppService");

        service = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("AppService")) {
                    Log.d("Radio", intent.getStringExtra("Data"));
                }
            }
        };
        getActivity().registerReceiver(service, filter);


    }

}
