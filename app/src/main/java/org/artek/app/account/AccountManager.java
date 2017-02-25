package org.artek.app.account;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Toast;

import org.artek.app.Global;
import org.artek.app.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AccountManager {
    final private static String LOGIN = "login.php";
    final private static String QR_SEND = "check_qr.php";
    final private static byte SUCCESS = 1;
    final private static byte ERROR_UNKNOWN = 0;
    final private static byte ERROR_BAD = -1;
    private static String SERVER_URL = "";
    private final OkHttpClient client = new OkHttpClient();
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
        SERVER_URL = activity.getString(R.string.main_domain) + "/artek/";
    }

    void login(String vkToken, String vkId) {
        this.vkToken = vkToken;
        this.vkId = vkId;
        login();
    }

    public void sendQR(String qr,int id,View view) {
        sendQR(qr);
        lastReciclerID = id;
        lastQR = qr;
        lastView = view;
    }

    void getUserInfo(String token) {
        vkToken = token;
        checkToken();
    }

    public void setAppInterface(Global.appInterface appInterface) {
        this.appInterface = appInterface;
    }

    private String rawQuery(URL url) throws IOException {
        InputStream input = url.openStream();
        Scanner in = new Scanner(input);
        return in.nextLine();
    }

    private String rawFullQuery(URL url) throws IOException {
        InputStream input = url.openStream();
        Scanner in = new Scanner(input);
        String answer = "";
        while (in.hasNextLine())
        answer = answer+"\n"+in.nextLine();
        return answer;
    }

    private AlertDialog.Builder generateMsg(byte result) {

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

    private void login() {
        final Request request = new Request.Builder()
                .url(SERVER_URL + LOGIN + "?vk_token=" + vkToken + "&vk_id=" + vkId)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    private void checkToken() {

        final Request request = new Request.Builder()
                .url("https://api.vk.com/method/groups.stats.trackVisitor?access_token=" + vkToken + "&v=5.60")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                    Toast.makeText(activity, activity.getString(R.string.not_connected),Toast.LENGTH_SHORT).show();
                }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String answer = response.body().string();
                if (answer.contains("response")) {
                    checkGroup();
                    Toast.makeText(activity, activity.getString(R.string.success_sign_in), Toast.LENGTH_SHORT).show();
                } else if (answer.contains("error")) {
                    Toast.makeText(activity, activity.getString(R.string.account_data_not_valid), Toast.LENGTH_SHORT).show();
                    appInterface.returner();
                    Global.sharedPreferences.edit().remove(Global.SharedPreferencesTags.LAST_TOKEN).apply();
                }

                }
            }

        );
    }

    private void checkGroup() {

        final Request request = new Request.Builder()
                .url("https://api.vk.com/method/groups.isMember?access_token=" + vkToken + "&v=5.60&group_id=132787995")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String answer = response.body().string();

                                                if (!answer.contains("1")) {

                                                    AlertDialog.Builder ad;
                                                    ad = new AlertDialog.Builder(activity);
                                                    ad.setTitle(activity.getString(R.string.subscribe_us));  // заголовок
                                                    ad.setMessage(activity.getString(R.string.subscribe_long)); // сообщение
                                                    ad.setPositiveButton(activity.getString(R.string.subscribe), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int arg1) {
                                                            subscribeGroup();
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

        );
    }

    private void subscribeGroup() {

        final Request request = new Request.Builder()
                .url("https://api.vk.com/method/groups.join?access_token=" + vkToken + "&v=5.60&group_id=132787995")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String answer = response.body().string();
                                                if (answer.contains("1")) {
                                                    Toast.makeText(activity, activity.getString(R.string.subscribe_thank), Toast.LENGTH_SHORT).show();
                                                }

            }
                                        }

        );
    }

    private void sendQR(String a) {

        final Request request = new Request.Builder()
                .url(SERVER_URL + QR_SEND + "?id=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID, null) + "&qr=" + a)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Отправка");
        progressDialog.show();


        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                progressDialog.cancel();
                                                Toast.makeText(Global.activity, "Не удалось отправить код, проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String string = response.body().string();

                                                progressDialog.cancel();

                                                AlertDialog.Builder dialog = null;
                                                if (string.contains("S")) {
                                                    dialog = generateMsg(SUCCESS).setMessage("Вы посетили точку '" + string.substring(2) + "'");
                                                } else if (string.contains("E5")) {
                                                    dialog = generateMsg(ERROR_BAD).setMessage("Вы уже посещали точку '" + string.substring(3) + "'");
                                                } else if (string.contains("E4")) {
                                                    dialog = generateMsg(ERROR_BAD).setMessage("Такой точки не существует");
                                                }

                                                if (dialog != null) {
                                                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            reciclerInterface.remove(lastReciclerID, lastQR, lastView);
                                                        }
                                                    });
                                                    dialog.show();
                                                }

                                            }
                                        }

        );
    }

    public interface ReciclerInterface {
        void remove(int id, String qr, View view);
    }

}

