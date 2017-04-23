package ru.temoteam.artek.app.main;


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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
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
import ru.temoteam.artek.app.AnalyticsApplication;
import ru.temoteam.artek.app.ExceptionHandler;
import ru.temoteam.artek.app.Global;
import ru.temoteam.artek.app.R;
import ru.temoteam.artek.app.adapters.RecyclerTipsAdapter;

public class TipsFragment extends ArtekFragment {

    RecyclerView rw;
    FloatingActionButton fab;
    View.OnClickListener fabListener;
    EditText et;
    NoInternetFragment nif;
    private String name = "Tips";


    public TipsFragment(){
        super.init(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        title = getString(R.string.tips);
        View result = inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_tips, null);
        rw = (RecyclerView) result.findViewById(ru.temoteam.artek.app.R.id.rw);
        rw.setLayoutManager(new LinearLayoutManager(getActivity()));
        fab = (FloatingActionButton) result.findViewById(ru.temoteam.artek.app.R.id.floatingActionButton);
        createListners();
        fab.setOnClickListener(fabListener);
        new TipsGet().execute();
        nif = new NoInternetFragment();
        nif.setFrom(this);

        return result;
    }

    private void createListners(){
        fabListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Написать совет");
                View v = View.inflate(getActivity(), ru.temoteam.artek.app.R.layout.add_tip, null);
                et = (EditText) v.findViewById(ru.temoteam.artek.app.R.id.editText);
                ;
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
        boolean needNew = true;

        public TipsGet(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (RecyclerTipsAdapter.data==null)
                data = new ArrayList<>();
            else {
                data = RecyclerTipsAdapter.data;
                data.clear();
                needNew = false;
            }
            try {
                Scanner in = new Scanner(new URL(Global.server+"artek/get_tips.php").openConnection().getInputStream());
                JSONArray jsonArray = new JSONArray(in.nextLine());

                String ids = "";

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jTip = jsonArray.getJSONObject(i);
                    HashMap<String,String> tip = new HashMap<String,String>();
                    tip.put("text",jTip.getString("text"));
                    tip.put("fromid",jTip.getString("fromid"));
                    tip.put("rate",jTip.getString("rating"));
                    tip.put("id",jTip.getString("id"));

                    if (ids.equals(""))
                        ids = tip.get("fromid");
                    else
                        ids+=","+tip.get("fromid");
                    data.add(tip);
                }

                if (needNew)
                publishProgress(new RecyclerTipsAdapter(data));


                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("v","5.62"));
                pairs.add(new BasicNameValuePair("fields","photo_200"));
                pairs.add(new BasicNameValuePair("user_ids",ids));

                JSONArray jArrayUsers = new JSONObject(getQuery(pairs,new URL("https://api.vk.com/method/users.get"))).getJSONArray("response");
                for (int i = 0;i<jArrayUsers.length();i++) {
                    JSONObject jUser = jArrayUsers.getJSONObject(i);
                    String id = jUser.getString("id");
                    for (int j = 0; j < data.size(); j++) {
                        HashMap<String, String> tip = data.get(j);
                        if (id.equals(tip.get("fromid"))){
                        tip.put("name", jUser.getString("first_name") + " " + jUser.getString("last_name"));
                        tip.put("img", jUser.getString("photo_200"));
                        }
                    }

                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                publishProgress(null);
                return false;
            }
        }

        private String getQuery(List<NameValuePair> params,URL url) throws Exception{
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
            String res = "";
            while (in.hasNextLine()){
                if (res.equals("")) res = in.nextLine();
                else res = res + " " + in.nextLine();
            }
            return res;
        }

        @Override
        protected void onProgressUpdate(RecyclerTipsAdapter... values) {
            super.onProgressUpdate(values);
            if (values!=null) {
                Log.i("setted","true");
                values[0].init(MainActivity.imageLoader,getActivity());
                rw.setAdapter(values[0]);
            }
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid)
                rw.getAdapter().notifyDataSetChanged();
            else {
                getFragmentManager().beginTransaction().replace(ru.temoteam.artek.app.R.id.frgmCont, nif).addToBackStack(null).commit();
            }
        }
    }

    class SendTip extends AsyncTask<Void,Void,Void>{
        String text;
        public SendTip(String text){
            this.text = text;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new TipsGet().execute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("id", Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null)));
                params.add(new BasicNameValuePair("text", text));

                URL url = new URL(Global.server+"artek/add_tip.php");

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