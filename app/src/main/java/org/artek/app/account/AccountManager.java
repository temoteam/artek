package org.artek.app.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.artek.app.Global;
import org.artek.app.R;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;


public class AccountManager {

    final public static String SERVER_URL = "http://lohness.com/artek/";
    final public static String LOGIN = "login.php";
    final public static String QR_SEND = "check_qr.php";

    final public static byte SUCCESS = 1;
    final public static byte ERROR_UNKNOWN = 0;
    final public static byte ERROR_BAD = -1;

    private Activity activity;
    private String vkToken;
    private String vkId;

    private Global.appInterface appInterface;
    private ReciclerInterface reciclerInterface;

    private int lastReciclerID;
    private String lastQR;
    private View lastView;

    public AccountManager(Activity activity){
        this.activity = activity;
    }

    public void login(String vkToken,String vkId){
        this.vkToken = vkToken;
        this.vkId = vkId;
        new Login().execute();
    }

    public void sendQR(String qr,int id,View view) {
        new SendQR().execute(qr);
        lastReciclerID = id;
        lastQR = qr;
        lastView = view;
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
        return answer;
    }

    private String rawFullQuery(URL url) throws IOException {
        InputStream input = url.openStream();
        Scanner in = new Scanner(input);
        String answer = "";
        while (in.hasNextLine())
        answer = answer+"\n"+in.nextLine();
        return answer;
    }

    public AlertDialog.Builder generateMsg(byte result){

        AlertDialog.Builder ad;
        ad = new AlertDialog.Builder(Global.activity);
        ad.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });

        if (result > 0) {{
            ad.setTitle(activity.getString(R.string.success));
        }
        } else {
            ad.setTitle(activity.getString(R.string.failed));
            ad.setMessage(activity.getString(R.string.not_connected));
        }
        return ad;
    }

    public void setReciclerInterface(ReciclerInterface reciclerInterface) {
        this.reciclerInterface = reciclerInterface;
    }

    class CheckToken extends AsyncTask<Void, Void, Byte>{

        @Override
        protected Byte doInBackground(Void... params) {
            try {

                URL url = new URL("https://api.vk.com/method/stats.trackVisitor?access_token="+vkToken+"&v=5.60");
                String answer = rawQuery(url);
                if (answer.equals("response: 1"))
                    return SUCCESS;
                else if(answer.contains("error"))
                    return ERROR_BAD;
                return  -128;
            } catch (IOException e) {
                e.printStackTrace();
                return  ERROR_UNKNOWN;
            }
        }

        @Override
        protected void onPostExecute(Byte aByte) {
            super.onPostExecute(aByte);
            if (aByte!=-128);
            switch (aByte){

                case ERROR_UNKNOWN:
                {
                    Toast.makeText(activity, activity.getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
                }
                case ERROR_BAD:
                {
                    Toast.makeText(activity, activity.getString(R.string.account_data_not_valid),Toast.LENGTH_SHORT).show();
                    appInterface.returner();
                    Global.sharedPreferences.edit().remove(Global.SharedPreferencesTags.LAST_TOKEN);
                }
                case SUCCESS:
                {
                    new CheckGroup().execute();
                    Toast.makeText(activity, activity.getString(R.string.success_sign_in),Toast.LENGTH_SHORT).show();
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
                ad.setTitle(activity.getString(R.string.subscribe_us));  // заголовок
                ad.setMessage(activity.getString(R.string.subscribe_long)); // сообщение
                ad.setPositiveButton(activity.getString(R.string.subscribe), new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int arg1) {
                       new SubscribeToGroup().execute();
                     }
                });
                ad.setCancelable(true);
                ad.setNegativeButton(activity.getString(R.string.later), new DialogInterface.OnClickListener() {
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
                Toast.makeText(activity, activity.getString(R.string.subscribe_thank),Toast.LENGTH_SHORT);
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

    class SendQR extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Отправка");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                    return rawQuery(new URL(SERVER_URL+QR_SEND+"?id="+Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null)+"&qr="+params[0]));
            } catch (IOException e) {
                return "EI";
            }
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            progressDialog.hide();
            AlertDialog.Builder dialog = null;
            if (string.equals("EI")) Toast.makeText(Global.activity,"Не удалось отправить код, проверьте подключение к интернету",Toast.LENGTH_SHORT).show();
            else if (string.contains("S")) {
                dialog = generateMsg(SUCCESS).setMessage("Вы посетили точку '"+ string.substring(2)+"'");
            }
            else if (string.contains("E5")) {
                dialog = generateMsg(ERROR_BAD).setMessage("Вы уже посещали точку '"+ string.substring(3)+"'");
            }
            else if (string.contains("E4")) {
                dialog = generateMsg(ERROR_BAD).setMessage("Такой точки не существует");
            }

                if (dialog!=null){
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            reciclerInterface.remove(lastReciclerID,lastQR,lastView);
                        }
                    });
                    dialog.show();
                }
        }
    }

    public interface ReciclerInterface{
        void remove(int id,String qr,View view);
    }

}

