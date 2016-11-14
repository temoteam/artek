package org.artek.app.main;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.account.FirstFragment;
import org.artek.app.account.LoginFragment;
import org.artek.app.account.SelectCampFragment;
import org.artek.app.game.GameActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {

            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Global.initilizate(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


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


        fTrans = getFragmentManager().beginTransaction();
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.CAMP)){
            selectCampFragment.setTheme(Global.sharedPreferences.getInt(Global.SharedPreferencesTags.CAMP,0),this);
            if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)){
                fTrans.replace(R.id.frgmCont, newsFragment);
                Global.accountManager.getUserInfo(Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null));}
            else
                fTrans.replace(R.id.frgmCont,new LoginFragment());}
        else
            fTrans.replace(R.id.frgmCont,selectCampFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();

        selectCampFragment.setAppInterface(new Global.appInterface() {
            @Override
            public void returner() {
                fTrans = getFragmentManager().beginTransaction();
                if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN))
                    fTrans.replace(R.id.frgmCont, newsFragment);
                else
                    fTrans.replace(R.id.frgmCont,new LoginFragment());

                fTrans.addToBackStack(null);
                fTrans.commit();
            }
        });

        new Updater(this);
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
            return true;
        }
        if (id == R.id.startGame) {
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

            fTrans.replace(R.id.frgmCont, newsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_dictionary) {

            fTrans.replace(R.id.frgmCont, dictFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_tips) {

            fTrans.replace(R.id.frgmCont, tipsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_radio) {

            fTrans.replace(R.id.frgmCont, radioFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        } else if (id == R.id.nav_settings) {

            fTrans.replace(R.id.frgmCont, settingsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
