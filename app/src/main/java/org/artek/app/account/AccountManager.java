package org.artek.app.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class AccountManager {

    final public static String SERVER_URL = "http://lohness.com/artek/";
    final public static String LOGIN = "login.php";

    final public static byte SUCCESS = 1;
    final public static byte ERROR_UNKNOWN = 0;

    private Activity activity;
    private String vkToken;
    private String vkId;

    public AccountManager(Activity activity){
        this.activity = activity;
    }

    public void login(String vkToken,String vkId){
        this.vkToken = vkToken;
        this.vkId = vkId;
        new Login().execute();
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


    class Login extends AsyncTask<Void, Void, Byte> {

        @Override
        protected Byte doInBackground(Void... params) {

            try {
                Log.i("",SERVER_URL+LOGIN+"?vk_token="+vkToken+"&vk_id="+vkId);
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

