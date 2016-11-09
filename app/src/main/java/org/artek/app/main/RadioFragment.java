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
import android.widget.Button;

import org.artek.app.R;
import org.artek.app.RadioService;

public class RadioFragment extends Fragment {

    boolean isPlaying = true;
    BroadcastReceiver service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio, null);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button radioButton = (Button) getActivity().findViewById(R.id.radioButton);
        radioButton.setOnClickListener(pausePlay);
        //Регистрация приемника
        IntentFilter filter = new IntentFilter();
        filter.addAction("AppService");

        service = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                if(intent.getAction().equals("AppService"))
                {
                    Log.d("AppService",intent.getStringExtra("Data"));
                }
            }
        };
        getContext().registerReceiver(service, filter);



    }

    private View.OnClickListener pausePlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isPlaying){
                getContext().startService(new Intent(getContext(),RadioService.class));
                isPlaying= false;
            } else {
                //if(service!= null){getContext().unregisterReceiver(service);}
                getContext().stopService(new Intent(getContext(),RadioService.class));
                isPlaying = true;
            }
        }
    };

}
