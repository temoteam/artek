package org.artek.app;

/**
 * Created by Sergey on 11.11.2016.
 */


import android.content.res.Resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final OkHttpClient client = new OkHttpClient();
    private Thread.UncaughtExceptionHandler defaultUEH;
    private String url;

    /*
     * if any of the parameters is null, the respective functionality
     * will not be used
     */
    public ExceptionHandler() {
        this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
    }

    public void uncaughtException(Thread t, Throwable e) {

        String timestamp = Calendar.getInstance().getTime().toString();
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        String filename = timestamp + ".stacktrace";


        //writeToFile(stacktrace, filename);
        sendLog(filename, stacktrace);


        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {

            BufferedWriter bos = new BufferedWriter(new FileWriter(new File(
                    "logs/" + filename)));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendLog(String filename, String message) {
        RequestBody formBody = new FormBody.Builder()
                .add("filename", filename)
                .add("stacktrace", message)
                .build();
        Request request = new Request.Builder()
                .url(Global.server + "xartek/log/upload.php")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

}