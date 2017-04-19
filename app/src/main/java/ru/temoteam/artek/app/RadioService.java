package ru.temoteam.artek.app;


import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;


public class RadioService extends Service  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener {
    final static String SENDMESAGGE = "passMessage";
    public static Boolean serviceStatus = false;
    public MediaPlayer mediaPlayer;


    public RadioService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
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

        mediaPlayer = new MediaPlayer();
        playMp3("http://azurecom.ru:8000/newradio");
    }


    public void onDestroy() {

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

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

        playMp3("http://azurecom.ru:8000/artek");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }



}
