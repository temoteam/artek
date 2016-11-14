package org.artek.app.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.artek.app.Global;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class AccountManager {

    final public static String SERVER_URL = "http://lohness.com/artek/";
    final public static String LOGIN = "login.php";

    final public static byte SUCCESS = 1;
    final public static byte ERROR_UNKNOWN = 0;
    final public static byte ERROR_BAD = -1;

    private Activity activity;
    private String vkToken;
    private String vkId;

    private Global.appInterface appInterface;

    public AccountManager(Activity activity){
        this.activity = activity;
    }

    public void login(String vkToken,String vkId){
        this.vkToken = vkToken;
        this.vkId = vkId;
        new Login().execute();
    }

    public void getUserInfo(String token){
        vkToken = token;
        new CheckToken().execute();
    }

    public void setAppInterface(Global.appInterface appInterface) {
        this.appInterface = appInterface;
    }

    private String rawQuery(URL url) throws IOException {
        InputStream input = url.openStream();
        Scanner in = new Scanner(input);
        return in.nextLine();
    }

    public AlertDialog.Builder generateMsg(byte result){

        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(activity);
        ad.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });

        if (result > 0) {{
            ad.setTitle("Успех !");
        }
        } else {
            ad.setTitle("Провалено");
            ad.setMessage("Проверьте подкючение к сети");
        }
        return ad;
    }

    class CheckToken extends AsyncTask<Void, Void, Byte>{

        @Override
        protected Byte doInBackground(Void... params) {
            try {
                URL url = new URL("https://api.vk.com/method/groups.join?group_id=44235988&not_sure=1&access_token="+vkToken+"&v=V");
                String answer = rawQuery(url);
                Log.i("VK CHECK url:",url +" answer: "+answer);
                if (answer.equals("{\"response\":1}"))
                    return SUCCESS;
                else return ERROR_BAD;
            } catch (IOException e) {
                e.printStackTrace();
                return  ERROR_UNKNOWN;
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            switch (aByte){



                case ERROR_UNKNOWN:
                {
                    Toast.makeText(activity,"Не удалось войти вконтакте \n проверьте соединение с интернетом",Toast.LENGTH_SHORT);
                }
                case ERROR_BAD:
                {
                    Toast.makeText(activity,"Не удалось войти вконтакте \n подтвердите ваши данные",Toast.LENGTH_SHORT);
                    appInterface.returner();
                    Global.sharedPreferences.edit().remove(Global.SharedPreferencesTags.LAST_TOKEN);
                }
                case SUCCESS:
                {
                    Toast.makeText(activity,"Вы успешно вошли в систему",Toast.LENGTH_SHORT);
                }
            }
        }
    }


    class Login extends AsyncTask<Void, Void, Byte> {

        @Override
        protected Byte doInBackground(Void... params) {

            try {
                if (rawQuery(new URL(SERVER_URL+LOGIN+"?vk_token="+vkToken+"&vk_id="+vkId)).equals("S"))
                return SUCCESS;
                else return ERROR_UNKNOWN;
            } catch (IOException e) {
                return ERROR_UNKNOWN;
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            generateMsg(aByte).show();
        }
    }
}

