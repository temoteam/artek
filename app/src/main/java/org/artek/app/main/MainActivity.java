package org.artek.app.main;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.GCMRegistrationIntentService;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.account.LoginFragment;
import org.artek.app.account.SelectCampFragment;
import org.artek.app.game.GameActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    int backButton = 0;

    DictFragment dictFragment;
    NewsFragment newsFragment;
    MessageFragment messageFragment;
    TipsFragment tipsFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;
    RadioFragment radioFragment;
    FragmentTransaction fTrans;
    SelectCampFragment selectCampFragment;
    String name = "MainActivity";
    Tracker mTracker;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        Global.initilizate(this);
        //checkTheme();
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.THEME_ID)) {
            Integer theme = Global.sharedPreferences.getInt(Global.SharedPreferencesTags.THEME_ID, 0);
            setTheme(theme);

        }
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_main);

        new ServerAlert(MainActivity.this).execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Global.initilizate(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    String token = intent.getStringExtra("token");
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_gcm), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            }
        };

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), getString(R.string.gps_not_installed), Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.gps_not_support), Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

        // fileRW = new FileRW(this);
        //int kekzaza = fileRW.readIntFile("theme");

        // laTheme(0);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        dictFragment = new DictFragment();
        newsFragment = new NewsFragment();
        messageFragment = new MessageFragment();
        tipsFragment = new TipsFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();
        radioFragment = new RadioFragment();
        selectCampFragment = new SelectCampFragment();


        Global.accountManager.setAppInterface(new Global.appInterface() {
            @Override
            public void returner() {
                fTrans = getFragmentManager().beginTransaction();
                fTrans.replace(R.id.frgmCont, new LoginFragment());
                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        selectCampFragment.setAppInterface(new Global.appInterface() {
            @Override
            public void returner() {
                fTrans = getFragmentManager().beginTransaction();
                if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN))
                    fTrans.replace(R.id.frgmCont, newsFragment);
                else
                    fTrans.replace(R.id.frgmCont, new LoginFragment());

                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.THEME_ID)) {
            Integer theme = Global.sharedPreferences.getInt(Global.SharedPreferencesTags.THEME_ID, 0);
            setTheme(theme);

        }

        fTrans = getFragmentManager().beginTransaction();
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.THEME_ID)) {
            setTheme(Global.sharedPreferences.getInt(Global.SharedPreferencesTags.THEME_ID, 0));
            if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)) {
                fTrans.replace(R.id.frgmCont, newsFragment);
                Global.accountManager.getUserInfo(Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null));
            } else
                fTrans.replace(R.id.frgmCont, new LoginFragment());
        } else
            fTrans.replace(R.id.frgmCont, selectCampFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();


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
        new Updater(this);
        Global.activity = this;


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.access_file_denied), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_camp) {
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, selectCampFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }
        if (id == R.id.startGame) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("StartGame")
                    .build());
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        fTrans = getFragmentManager().beginTransaction();

        if (id == R.id.nav_news) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("OpenNews")
                    .build());
            fTrans.replace(R.id.frgmCont, newsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_dictionary) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("OpenDictionary")
                    .build());
            fTrans.replace(R.id.frgmCont, dictFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_tips) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("OpenTips")
                    .build());
            fTrans.replace(R.id.frgmCont, tipsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_radio) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("OpenRadio")
                    .build());
            fTrans.replace(R.id.frgmCont, radioFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        } else if (id == R.id.nav_settings) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("OpenSettings")
                    .build());
            fTrans.replace(R.id.frgmCont, settingsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    class ServerAlert extends AsyncTask<Void, Void, String> {

        private Context context;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        private String LOG_TAG = "ServerAlert";

        public ServerAlert(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... params) {
            // получаем данные с внешнего ресурса

            Log.d("alert", "async");
            try {
                URL url = new URL("http://lohness.com/artek/alertdata.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(String strJson) {
            super.onPostExecute(strJson);
            // выводим целиком полученную json-строку
            Log.d("alert", strJson);

            JSONObject dataJsonObj = null;

            try {
                dataJsonObj = new JSONObject(strJson);
                Boolean isMsg = dataJsonObj.getBoolean("isMsg");
                if (isMsg) {
                    final JSONObject msg = dataJsonObj.getJSONObject("msg");
                    String header = msg.getString("header");
                    String body = msg.getString("body");
                    Boolean isUrl = msg.getBoolean("isUrl");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog = dialog
                            .setTitle(header)
                            .setMessage(body + "\n" + msg.getString("url"))
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    if (isUrl) {
                        dialog.setPositiveButton(R.string.proceed, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String url = null;
                                try {
                                    url = msg.getString("url");
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    context.startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    dialog.setCancelable(false);
                    dialog.show();
                    Log.d("alert", header + " " + body);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}