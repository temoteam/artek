package org.artek.app.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.artek.app.Global;
import org.artek.app.R;


import java.io.DataOutputStream;

import java.net.HttpURLConnection;

import java.net.URL;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

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
            new Send2VK().execute(new String[]{Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null),text});
        }   else if (type == 1){
            new Send2Server().execute(new String[]{Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,"noid"),text});
        }
    }

    private class Send2VK extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {

            try {
                String urlParameters = "v=5.60&access_token="+params[0]+"&peer_id=-132787995&message="+params[1];
                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int    postDataLength = postData.length;
                String request = "https://api.vk.com/method/messages.send";
                URL url = new URL(request);
                HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                conn.setDoOutput( true );
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                DataOutputStream wr = new DataOutputStream( conn.getOutputStream()) ;
                wr.write( postData );
                if (conn.getResponseCode()==200)
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
                Toast.makeText(activity,"Сообщение успешно доставлено",Toast.LENGTH_SHORT);
        }
    }

    private class Send2Server extends AsyncTask<String,Void,Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String urlParameters = "id=" + params[0] + "&msg=" + params[1];
                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int postDataLength = postData.length;
                String request = "http://lohness.com/artek/msg/send.php";
                URL url = new URL(request);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
                if (conn.getResponseCode() == 200)
                    return true;

            }
            catch (Exception e){e.printStackTrace();}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
                Toast.makeText(activity,"Сообщение успешно доставлено",Toast.LENGTH_SHORT);
        }
    }

}
