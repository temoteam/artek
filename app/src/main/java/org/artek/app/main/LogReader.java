package org.artek.app.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class LogReader implements Thread.UncaughtExceptionHandler {

    public final static String FILENAME = "logs";


    private LogReaderTask logReaderTask = null;
    Activity activity;
    FileOutputStream fos;



    public LogReader (Activity activity) throws FileNotFoundException {
        this.activity = activity;
        fos  = activity.openFileOutput(FILENAME, Context.MODE_PRIVATE);
        start();
        Thread.setDefaultUncaughtExceptionHandler (this);
    }



    private void start() {

        File logs = new File(FILENAME);
        if (logs.exists())
            logs.delete();

        logReaderTask = new LogReaderTask();
        logReaderTask.execute();
    }

    private void add(String log) throws IOException {
        fos.write(log.getBytes());
    }


    public void stop() throws IOException {
        logReaderTask.stopTask();
        fos.close();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent ();
        intent.setAction ("org.artek.app.SEND_LOG");
        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity (intent);
        System.exit(1);
    }


    private class LogReaderTask extends AsyncTask<Void, String, Void> {
        private final String[] LOGCAT_CMD = new String[] { "logcat" };
        private final int BUFFER_SIZE = 1024;

        private boolean isRunning = true;
        private Process logprocess = null;
        private BufferedReader reader = null;
        private String[] line = null;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                logprocess = Runtime.getRuntime().exec(LOGCAT_CMD);
            } catch (IOException e) {
                e.printStackTrace();

                isRunning = false;
            }

            try {
                reader = new BufferedReader(new InputStreamReader(
                        logprocess.getInputStream()), BUFFER_SIZE);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

                isRunning = false;
            }

            line = new String[1];

            try {
                while (isRunning) {
                    line[0] = reader.readLine();

                    publishProgress(line);
                }
            } catch (IOException e) {
                e.printStackTrace();

                isRunning = false;
            }

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values[0]!=null){
                  try {
                      add("\n" +values[0]);
                  } catch (IOException e) {
                     e.printStackTrace();
                  }
            }
        }

        public void stopTask() {
            isRunning = false;
            logprocess.destroy();
        }
    }


}