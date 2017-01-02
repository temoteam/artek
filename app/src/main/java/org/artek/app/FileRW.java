package org.artek.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by rooh1 on 18-Dec-16.
 */

public final class FileRW {
    private final String IO_LOG_TAG = "FileRW";
    private Context context;
    private Activity activity;


    public FileRW(Activity activity) {
        this.context = activity.getApplicationContext();
        this.activity = activity;
    }
    public FileRW(Context context) {
        this.context = this.context;
    }
    public void writeFile(String FILENAME, String content) {
        try {

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.openFileOutput(FILENAME, MODE_PRIVATE)));

            bw.write(content);

            bw.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {

                content = content + str;
            }

            // close stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

}

