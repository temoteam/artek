package org.artek.app.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.artek.app.Global;
import org.artek.app.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CallBack {

    private Activity activity;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private int type;

    private CheckBox checkBox;
    private EditText editText;


    public CallBack(Activity activity){
        this.activity = activity;
    }

    public void startCallBack(){
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.IS_CALLBACK_SAVED)){
            type = Global.sharedPreferences.getInt(Global.SharedPreferencesTags.IS_CALLBACK_SAVED,-1);
            resumeCallBack();}
        else {
            View view = activity.getLayoutInflater().inflate(R.layout.dialog_callback_typeselect,null);
            ListView list = (ListView) view.findViewById(R.id.listView2);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            list.setAdapter(new ArrayAdapter<String>(activity,android.R.layout.simple_list_item_1, new String[]{"Через вконтакте (рекомендуется)", "На сервер"}));
            builder = new AlertDialog.Builder(activity);
            builder.setTitle("Выберите способ отправки");
            builder.setView(view);
            dialog = builder.show();

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    dialog.hide();
                    type = position;
                    if (checkBox.isChecked())
                        Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.IS_CALLBACK_SAVED,type).apply();
                    resumeCallBack();
                }
            });

        }

    }

    private void resumeCallBack(){
        View view = activity.getLayoutInflater().inflate(R.layout.dialog_callback_text,null);

        editText = (EditText) view.findViewById(R.id.editText);
        Button button = (Button)view.findViewById(R.id.button2);
        builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        dialog = builder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
                endCallBack(editText.getText().toString());
            }
        });

    }

    private void endCallBack(String text) {
        if (type == 0) {
            new SendVK().execute(new String[]{Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null),text});
        }   else if (type == 1){
            //тут отправка на сервер
        }
    }

    private class SendVK extends AsyncTask<String,Void,Boolean>{

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                List<NameValuePair> data = new ArrayList<NameValuePair>();
                data.add(new BasicNameValuePair("access_token", params[0]));
                data.add(new BasicNameValuePair("peer_id", "-132787995"));
                data.add(new BasicNameValuePair("message", params[1]));
                data.add(new BasicNameValuePair("charset", "utf-8"));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost http = new HttpPost("https://api.vk.com/method/messages.send?v=5.60");
                http.setEntity(new UrlEncodedFormEntity(data));
                httpclient.execute(http, new BasicResponseHandler());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}
