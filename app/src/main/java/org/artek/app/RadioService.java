package org.artek.app;


import android.app.Service;
import android.content.Intent;
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


public class RadioService extends Service {
    String a[];
    public MediaPlayer mediaPlayer;

    final Random random = new Random();

    boolean intialStage = true;

    final static String SENDMESAGGE = "passMessage";

    public static Boolean serviceStatus = false;


    public RadioService() {
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //you service etc...
        return Service.START_STICKY;
    }
    private void passMessageToActivity(String message){
        Intent intent = new Intent();
        intent.setAction(SENDMESAGGE);
        intent.putExtra("message",message);
        sendBroadcast(intent);
    }




    public void onCreate() {
        super.onCreate();
    }


    public void onDestroy() {
        super.onDestroy();
    }









    class Player extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mediaPlayer.setDataSource(params[0]);

                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        intialStage = true;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        int e = random.nextInt(a.length);
                        new Player()
                                .execute("http://lohness.com/artek/radio/files/"+a[e]);
                    }
                });
                mediaPlayer.prepare();
                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.d("Prepared", "//" + result);
            mediaPlayer.start();

            intialStage = false;
        }

        public Player() {
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }
    }



    class GetSongs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {

            String content;
            try{

                content = getContent("http://lohness.com/artek/radio/index.php");
            }
            catch (IOException ex){
                content = ex.getMessage();
            }

            return content;
        }
        @Override
        protected void onProgressUpdate(Void... items) {
        }
        @Override
        public void onPostExecute(String content) {

            a = content.split("#");
            int e = random.nextInt(a.length);
            new Player()
                    .execute("http://lohness.com/artek/radio/files/"+a[e]);

        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try {
                URL url=new URL(path);
                HttpURLConnection c=(HttpURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf=new StringBuilder();
                String line=null;
                while ((line=reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return(buf.toString());
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}
