package org.artek.app;

/**
 * Created by Sergey on 11.11.2016.
 */


import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

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

        writeToFile(stacktrace, filename);


            new SendLOG(filename, stacktrace).execute();


        defaultUEH.uncaughtException(t, e);
    }

    private void writeToFile(String stacktrace, String filename) {
        try {
            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    "logs/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SendLOG extends AsyncTask<String, String, String> {

        String filename;
        String stacktrace;
        public SendLOG(String filename, String stacktrace) {
            this.filename = filename;
            this.stacktrace = stacktrace;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {


            String resultToDisplay = "";


            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://lohness.com/artek/log/upload.php");
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                nvps.add(new BasicNameValuePair("filename", filename));
                nvps.add(new BasicNameValuePair("stacktrace", stacktrace));

                try {
                    httpPost.setEntity(
                            new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                    HttpEntity response = httpClient.execute(httpPost).getEntity();

                    try{
                        InputStream in = (InputStream) response.getContent();
                        //Header contentEncoding = Response.getFirstHeader("Content-Encoding");
                    /*if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                        in = new GZIPInputStream(in);
                    }*/
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder str = new StringBuilder();
                        String line = null;
                        while((line = reader.readLine()) != null){
                            str.append(line + "\n");
                        }
                        in.close();
                        String resp = str.toString();

                    }
                    catch(IllegalStateException exc){

                        exc.printStackTrace();
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {

                System.out.println(e.getMessage());

                return e.getMessage();

            }
            return resultToDisplay;
        }


        @Override
        protected void onPostExecute(String result) {
        }
    }
}