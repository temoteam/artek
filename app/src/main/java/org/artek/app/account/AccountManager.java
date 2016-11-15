package org.artek.app.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.artek.app.Global;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
        String answer = in.nextLine();
        Log.i ("URL IS",url+" RESULT IS: "+answer);
        return answer;
    }

    private String rawFullQuery(URL url) throws IOException {
        InputStream input = url.openStream();
        Scanner in = new Scanner(input);
        String answer = "";
        while (in.hasNextLine())
        answer = answer+"\n"+in.nextLine();
        Log.i ("URL IS",url+" RESULT IS: "+answer);
        return answer;
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

                //URL url = new URL("https://api.vk.com/method/groups.join?group_id=132787995&not_sure=1&access_token="+vkToken+"&v=V");
                URL url = new URL("https://api.vk.com/method/stats.trackVisitor?access_token="+vkToken+"&v=5.60");
                String answer = rawQuery(url);
                if (answer.equals("response: 1"))
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
                    Toast.makeText(activity,"Не удалось войти вконтакте \n проверьте соединение с интернетом",Toast.LENGTH_SHORT).show();
                }
                case ERROR_BAD:
                {
                    Toast.makeText(activity,"Не удалось войти вконтакте \n подтвердите ваши данные",Toast.LENGTH_SHORT).show();
                    appInterface.returner();
                    Global.sharedPreferences.edit().remove(Global.SharedPreferencesTags.LAST_TOKEN);
                }
                case SUCCESS:
                {
                    new CheckGroup().execute();
                    Toast.makeText(activity,"Вы успешно вошли в систему",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class CheckGroup extends AsyncTask<Void, Void, Byte>{

        @Override
        protected Byte doInBackground(Void... params) {

            try {
                URL url = new URL("https://api.vk.com/method/groups.isMember?access_token="+vkToken+"&v=5.60&group_id=132787995");
                String answer = rawQuery(url);
                if (answer.equals("{\"response\":1}"))
                    return SUCCESS;
                else return ERROR_BAD;
            } catch (IOException e) {
                e.printStackTrace();
                return ERROR_UNKNOWN;
            }

        }


        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte==ERROR_BAD){
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(activity);
                ad.setTitle("Подпишитесь на нас в вк !");  // заголовок
                ad.setMessage("В нашей вы будите в курсе всех обновлений \n также мы будем благодарны за вашу поддержку"); // сообщение
                ad.setPositiveButton("Подписаться", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int arg1) {
                       new SubscribeToGroup().execute();
                     }
                });
                ad.setCancelable(true);
                ad.setNegativeButton("Потом", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                  }
              });
              ad.show();
            }
        }
    }

    class SubscribeToGroup extends AsyncTask<Void, Void, Byte>{
        @Override
        protected Byte doInBackground(Void... params) {
            try {
                URL url = new URL("https://api.vk.com/method/groups.join?access_token="+vkToken+"&v=5.60&group_id=132787995");
                String answer = rawQuery(url);
                if (answer.equals("response: 1"))
                    return SUCCESS;
                else return ERROR_BAD;
            } catch (IOException e) {
                e.printStackTrace();
                return ERROR_UNKNOWN;
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte==SUCCESS){
                Toast.makeText(activity,"Спасибо что подписались на нашу группу",Toast.LENGTH_SHORT);
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

