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

import org.artek.app.account.FirstFragment;
import org.artek.app.R;
import org.artek.app.Global;
import org.artek.app.game.GameActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final String LOG_TAG = "myLogs";

    final String FILENAME = "file";

    int backButton = 0;

    DictFragment dictFragment;
    NewsFragment newsFragment;
    MessageFragment messageFragment;
    TipsFragment tipsFragment;
    HistoryFragment historyFragment;
    SettingsFragment settingsFragment;
    RadioFragment radioFragment;
    FragmentTransaction fTrans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

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

        new Updater(this);

        /*if (Global.userInfo!=null)
            Global.accountManager.login(Global.userInfo.get("utoken"),Global.userInfo.get("uid"));*/


        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput("first")));

            // read data
            while ((str = br.readLine()) != null) {
                content = content + str;
            }

            // close stream
            br.close();

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, newsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            getFragmentManager().beginTransaction().replace(R.id.frgmCont, new FirstFragment()).commit();

        } catch (IOException e) {
            e.printStackTrace();
        }

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

        if (id == R.id.nav_news) {


            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, newsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_dictionary) {

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, dictFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        } else if (id == R.id.nav_tips) {

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, tipsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();

        }else if (id == R.id.nav_radio) {
            Log.d("lala", "lala");
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont,radioFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }else if (id == R.id.nav_settings) {

            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmCont, settingsFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {
                content = content + str;
            }

            // close stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            writeFile(FILENAME, "0");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void writeFile(String FILENAME, String content) {
        try {

            // open stream to write data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE)));

            // write data
            bw.write(content);

            // close stream
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
