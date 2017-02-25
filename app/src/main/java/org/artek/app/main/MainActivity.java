package org.artek.app.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.GCMRegistrationIntentService;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.account.LoginFragment;
import org.artek.app.account.LoginVKFragment;
import org.artek.app.account.SelectCampFragment;
import org.artek.app.game.DetailSpotFragment;
import org.artek.app.game.ScannerQRActivity;
import org.artek.app.game.StartGameFragment;
import org.artek.app.game.VisitedFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final OkHttpClient client = new OkHttpClient();
    int backButton = 0;
    Activity activity;
    DictFragment dictFragment;
    NewsFragment newsFragment;
    TipsFragment tipsFragment;
    SettingsFragment settingsFragment;
    VisitedFragment visitedFragment;
    DetailSpotFragment detailSpotFragment;
    RadioFragment radioFragment;
    LoginFragment loginFragment;
    StartGameFragment startGameFragment;
    LoginVKFragment loginVKFragment;
    org.artek.app.main.Callback callBack;
    FloatingActionButton fab;
    ImageView ava;
    TextView title;
    TextView email;
    FragmentTransaction fTrans;
    SelectCampFragment selectCampFragment;
    String name = "MainActivity";
    Tracker mTracker;
    ImageLoader imageLoader;
    private Snackbar mSnackbar;
    View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (loginVKFragment == null) loginVKFragment = new LoginVKFragment();
            fTrans = getFragmentManager().beginTransaction().replace(R.id.frgmCont, loginVKFragment).addToBackStack(null);
            fTrans.commit();
            mSnackbar.dismiss();
        }
    };
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Global.initilizate(this);
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.THEME_ID)) {
            Integer theme = Global.sharedPreferences.getInt(Global.SharedPreferencesTags.THEME_ID, 0);
            setTheme(theme);
        }

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginVKFragment == null) loginVKFragment = new LoginVKFragment();
                fTrans = getFragmentManager().beginTransaction();
                fTrans = fTrans.replace(R.id.frgmCont, loginVKFragment);
                fTrans.addToBackStack(null);
                fTrans.commit();
                mSnackbar.dismiss();

            }
        };


        activity = this;
        Global.initilizate(this);

        //checkServerAlert();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        if (!Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)) {
            mSnackbar = Snackbar
                    .make(this.findViewById(R.id.mainview), "Для игры необходима авторизация", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Войти с вк", snackbarOnClickListener)
                    .setActionTextColor(Color.parseColor("#ff4081")); // цвет текста у кнопки действия
            mSnackbar.show();

        }
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                    SharedPreferences sPref = Global.sharedPreferences;
                    SharedPreferences.Editor ed = sPref.edit();
                    ed.putString(Global.SharedPreferencesTags.GOOGLE_TOKEN, token);
                    ed.apply();
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_gcm), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), getString(R.string.gps_not_installed), Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.gps_not_support), Toast.LENGTH_LONG).show();
            }
        } else {
            Intent intent = new Intent(this, GCMRegistrationIntentService.class);
            startService(intent);
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ava = (ImageView) headerLayout.findViewById(R.id.imageView);
        title = (TextView) headerLayout.findViewById(R.id.name);
        email = (TextView) headerLayout.findViewById(R.id.email);


        Global.accountManager.setAppInterface(new Global.appInterface() {
            @Override
            public void returner() {
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, new LoginFragment());
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScannerQRActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        select(R.id.nav_news);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.FIRST_NAME)){
            title.setText(Global.sharedPreferences.getString(Global.SharedPreferencesTags.FIRST_NAME,"") + " " + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_NAME,""));
            email.setText(Global.sharedPreferences.getString(Global.SharedPreferencesTags.STATUS,""));
            imageLoader.displayImage(Global.sharedPreferences.getString(Global.SharedPreferencesTags.AVA_URL,"http://scontent.cdninstagram.com/t51.2885-19/s150x150/14693871_1109195849129674_3114733999868608512_a.jpg"),ava);
        }

        getUserInfo();

        //new Updater(this);

        //Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.OFF);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        if (backButton == 0) {
            backButton++;
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        Global.activity = this;

    }



    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camp) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, new SelectCampFragment());
            fTrans.addToBackStack(null);
            fTrans.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        select(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getUserInfo(){
        final Request request = new Request.Builder()
                .url("https://api.vk.com/method/users.get?v=5.62&fields=photo_200,status&user_ids=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null))
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String answer = response.body().string();
                    Log.i("user","https://api.vk.com/method/users.get?v=5.62&fields=photo_200,status&user_ids=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null));
                    Log.i("user",answer);
                    JSONObject jUser = new JSONObject(answer).getJSONArray("response").getJSONObject(0);
                    Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.FIRST_NAME,jUser.getString("first_name"))
                            .putString(Global.SharedPreferencesTags.LAST_NAME,jUser.getString("last_name"))
                            .putString(Global.SharedPreferencesTags.AVA_URL,jUser.getString("photo_200"))
                            .putString(Global.SharedPreferencesTags.STATUS,jUser.getString("status")).apply();


                }
                catch (Exception e){e.printStackTrace();}
            }
        });

    }

    public void select(int id) {

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 1);
        fTrans = getFragmentManager().beginTransaction();
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.CAMP)) {
            if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)) {
                // if (true){ //debug

                if (id == R.id.nav_visited) {
                    if (visitedFragment == null) visitedFragment = new VisitedFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, visitedFragment);
                    fab.show();
                } else if (id == R.id.nav_news) {
                    if (newsFragment == null) newsFragment = new NewsFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, newsFragment);
                    fab.hide();
                } else if (id == R.id.nav_dictionary) {
                    if (dictFragment == null) dictFragment = new DictFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, dictFragment);
                    fab.hide();
                } else if (id == R.id.nav_tips) {
                    if (tipsFragment == null) tipsFragment = new TipsFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, tipsFragment);
                    fab.hide();
                } else if (id == R.id.nav_radio) {
                    if (radioFragment == null) radioFragment = new RadioFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, radioFragment);
                    fab.hide();
                } else if (id == R.id.nav_leaderboard) {
                    if (detailSpotFragment == null) detailSpotFragment = new DetailSpotFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, detailSpotFragment);
                    fab.show();
                } else if (id == R.id.nav_allpoints) {
                    if (startGameFragment == null) startGameFragment = new StartGameFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, startGameFragment);
                    fab.show();
                } else if (id == R.id.nav_settings) {
                    if (settingsFragment == null) settingsFragment = new SettingsFragment();
                    fTrans = fTrans.replace(R.id.frgmCont, settingsFragment);
                    fab.hide();
                } else if (id == R.id.nav_callback) {
                    fTrans = fTrans.replace(R.id.frgmCont, callBack);
                    fab.hide();
                }


            } else {
                if (loginFragment == null) loginFragment = new LoginFragment();
                fTrans = fTrans.replace(R.id.frgmCont, loginFragment);
                Toast.makeText(this, "Для игры необходима авторизация", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (selectCampFragment == null) selectCampFragment = new SelectCampFragment();
            fTrans = fTrans.replace(R.id.frgmCont, selectCampFragment);
            Toast.makeText(this, "Для игры необходимо выбрать лагерь", Toast.LENGTH_SHORT).show();
        }
        fTrans.addToBackStack(null);
        fTrans.commit();

    }

    private void checkServerAlert() {
        Request request = new Request.Builder()
                .url(getString(R.string.main_domain) + "artek/alertdata.json")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String message = response.body().string();
                //System.out.print(message);
                JSONObject dataJsonObj;

                try {
                    dataJsonObj = new JSONObject(message);
                    final int isMsg = dataJsonObj.getInt("msgId");
                    if ((isMsg != 0) && Global.sharedPreferences.getInt(Global.SharedPreferencesTags.ALERT_ID, 0) != isMsg) {
                        final JSONObject msg = dataJsonObj.getJSONObject("msg");
                        String header = msg.getString("header");
                        String body = msg.getString("body");
                        Boolean isUrl = msg.getBoolean("isUrl");
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog = dialog
                                .setTitle(header)
                                .setMessage(body + "\n" + msg.getString("url"))
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.ALERT_ID, isMsg).apply();
                                    }
                                });
                        if (isUrl) {
                            dialog.setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String url;
                                    try {
                                        url = msg.getString("url");
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        dialog.setCancelable(true);
                        dialog.show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    }
                }
            }

        );
    }
}