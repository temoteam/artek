package org.artek.app.main;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.RadioService;

import static android.content.Context.DOWNLOAD_SERVICE;


public class RadioFragment extends Fragment {

    boolean isPlaying = false;
    BroadcastReceiver service;
    ImageButton radioButton;

    RadioTextFiller radioTextFiller;
    TextView name;
    ImageButton download;


    private View.OnClickListener pausePlay = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isPlaying) {
                getActivity().getApplicationContext().stopService(new Intent(getActivity(), RadioService.class));
                isPlaying = false;
                radioButton.setImageResource(R.drawable.ic_play);
                Log.d("radio", "sendStop");
            } else {
                //if(service!= null){getContext().unregisterReceiver(service);}
                getActivity().getApplicationContext().startService(new Intent(getActivity(), RadioService.class));
                isPlaying = true;
                radioButton.setImageResource(R.drawable.ic_pause);
                Log.d("radio","sendStart");
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_radio, null);
        download = (ImageButton) result.findViewById(R.id.Radio_download);
        name = (TextView) result.findViewById(R.id.Radio_text2);/*
        TextView tw = (TextView) result.findViewById(R.id.Radio_text1);
        tw.setText("Сейчас играет:");*/
        radioTextFiller = new RadioTextFiller(name);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                String filename = (String) name.getText();
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse("https://azurecom.ru/radio/"+filename+".mp3"));
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                r.allowScanningByMediaScanner();
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
            }
        });
        return result;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isPlaying) {
            getActivity().getApplicationContext().stopService(new Intent(getActivity(), RadioService.class));
            isPlaying = false;
            radioButton.setImageResource(R.drawable.ic_pause);
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        radioButton = (ImageButton) getActivity().findViewById(R.id.Radio_start);
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
