package org.artek.app.main;

import android.os.AsyncTask;
import android.widget.TextView;

import org.artek.app.Global;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by AlexS on 25.12.2016.
 */

class RadioTextFiller {
    private TextView textView;

    RadioTextFiller(TextView textView) {
        this.textView=textView;
        TextUpdating textUpdating = new TextUpdating();
        textUpdating.execute();
    }

    private class TextUpdating extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            while(true){
                try {
                    URL url = new URL("https://azurecom.ru/lala.php");
                    InputStream input = url.openStream();
                    Scanner in = new Scanner(input);
                    String answer = in.nextLine();
                    publishProgress(answer);
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textView.setText(values[0]);
            Global.nowPlaying = values[0];
        }
    }


}
