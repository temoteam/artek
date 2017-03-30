package org.artek.app.main;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.BooleanResult;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerTipsAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class TipsFragment extends Fragment {

    RecyclerView rw;
    FloatingActionButton fab;
    View.OnClickListener fabListener;
    EditText et;



    private String name = "Tips";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        View result = inflater.inflate(R.layout.fragment_tips, null);
        rw = (RecyclerView) result.findViewById(R.id.rw);
        rw.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = (FloatingActionButton) result.findViewById(R.id.floatingActionButton);
        createListners();
        fab.setOnClickListener(fabListener);
        new TipsGet().execute();
        return result;
    }

    private void createListners(){
        fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Написать совет");
                View v = View.inflate(getActivity(),R.layout.add_tip,null);
                et = (EditText) v.findViewById(R.id.editText);;
                builder.setView(v);
                builder.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("sending",et.getText().toString());
                        new SendTip(et.getText().toString()).execute();
                    }
                });
                builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityCreated(Bundle b){
        super.onActivityCreated(b);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    class TipsGet extends AsyncTask<Void,RecyclerTipsAdapter,Boolean>{

        List<HashMap<String,String>> data;

        public TipsGet(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            data = new ArrayList<>();
            try {
                Scanner in = new Scanner(new URL("https://azurecom.ru/artek/get_tips.php").openConnection().getInputStream());
                JSONArray jsonArray = new JSONArray(in.nextLine());

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jTip = jsonArray.getJSONObject(i);
                    HashMap<String,String> tip = new HashMap<String,String>();
                    tip.put("text",jTip.getString("text"));
                    tip.put("fromid",jTip.getString("fromid"));
                    tip.put("mark",jTip.getString("mark"));
                    tip.put("liked",jTip.getString("liked"));
                    tip.put("dliked",jTip.getString("dliked"));
                    data.add(tip);
                }

                publishProgress(new RecyclerTipsAdapter(data));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(null);
                return false;
            }
        }

        @Override
        protected void onProgressUpdate(RecyclerTipsAdapter... values) {
            super.onProgressUpdate(values);
            if (values!=null) {
                Log.i("setted","true");
                values[0].init(MainActivity.imageLoader);
                rw.setAdapter(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class SendTip extends AsyncTask<Void,Void,Void>{
        String text;
        public SendTip(String text){
            this.text = text;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("id", Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null)));
                params.add(new BasicNameValuePair("text", text));

                URL url = new URL("https://azurecom.ru/artek/add_tip.php");

                StringBuilder result = new StringBuilder();
                boolean first = true;

                for (NameValuePair pair : params)
                {
                    if (first)
                        first = false;
                    else
                        result.append("&");

                    result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                    result.append("=");
                    result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
                }

                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(result.toString());
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                Scanner in = new Scanner(conn.getInputStream());

                Log.i("status",conn.getResponseCode()+"");


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}