package ru.temoteam.artek.app.main;

import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.temoteam.artek.app.Global;

/**
 * Created by AlexS on 25.12.2016.
 */

class RadioTextFiller {
    private final OkHttpClient client = new OkHttpClient();
    private TextView textView;

    RadioTextFiller(TextView textView) {
        this.textView=textView;
        textUpdater();
    }


    private void textUpdater() {

        final Request request = new Request.Builder()
                .url(Global.server + "lala.php")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                textView.setText(responseString);
                Global.nowPlaying = responseString;

            }
            }

        );
    }


}
