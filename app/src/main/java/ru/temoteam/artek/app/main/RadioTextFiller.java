package ru.temoteam.artek.app.main;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import ru.temoteam.artek.app.Global;

/**
 * Created by AlexS on 25.12.2016.
 */

public class RadioTextFiller {
    TextView textView;
    public RadioTextFiller(TextView textView){
        this.textView=textView;
        TextUpdating textUpdating = new TextUpdating();
        textUpdating.execute();
    }

    class TextUpdating extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            while(true){
                try {
                    Thread.sleep(1000);
                    URL url = new URL(Global.server+"lala.php");
                    InputStream input = url.openStream();
                    Scanner in = new Scanner(input);
                    String answer = in.nextLine();
                    publishProgress(answer);
                } catch (Exception e) {

                }
            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textView.setText(values[0]);
        }
    }


}