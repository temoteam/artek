package org.artek.app;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


public class RadioService extends Service  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    final static String SENDMESAGGE = "passMessage";
    public static Boolean serviceStatus = false;
    public MediaPlayer mediaPlayer;


    public RadioService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //you service etc...

        return Service.START_STICKY;
    }

    private void passMessageToActivity(String message) {
        Intent intent = new Intent();
        intent.setAction(SENDMESAGGE);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }


    public void onCreate() {
        super.onCreate();
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        Log.d("radio","createService");
        mediaPlayer = new MediaPlayer();
        playMp3("http://azurecom.ru:8000/newradio");
    }


    public void onDestroy() {
        Log.d("radio", "destroyService");
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }


    public void playMp3(String _link){

        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(_link);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            //mediaPlayer.prepare(); // might take long! (for buffering, etc)   //@@
            mediaPlayer.prepareAsync();
            Log.d("radio", "startingRadio");
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block///
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        playMp3("http://azurecom.ru:8000/newradio");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }



}
