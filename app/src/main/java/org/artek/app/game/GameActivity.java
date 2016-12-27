package org.artek.app.game;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.account.LoginFragment;
import org.artek.app.account.SelectCampFragment;
import org.artek.app.main.MainActivity;
import org.artek.app.main.RadioFragment;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String name = "GameActivity";

    public static String SAVED = "saved_qr";

    int backButton = 0;
    int DIALOG_CONG = 11;


    FragmentTransaction fTrans;
    VisitedFragment visitedFragment;
    DetailSpotFragment detailSpotFragment;
    RadioFragment radioFragment;
    LoginFragment loginFragment;
    SelectCampFragment selectCampFragment;
    StartGameFragment startGameFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }

        super.onCreate(savedInstanceState);

        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.THEME_ID)) {
            Integer theme = Global.sharedPreferences.getInt(Global.SharedPreferencesTags.THEME_ID, 0);
            setTheme(theme);
        }

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        setContentView(R.layout.activity_game);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.game_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.game_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameActivity.this,ScannerQRActivity.class);
                startActivityForResult(intent, 0);
                select(R.id.nav_visited);
        }});

        select(R.id.nav_visited);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Global.activity = this;
    }



    public void select(int id) {

        fTrans = getFragmentManager().beginTransaction();
        if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.CAMP)) {
            if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)) {

                if (id == R.id.nav_visited){
                    if (visitedFragment==null) visitedFragment=new VisitedFragment();
                    fTrans = fTrans.replace(R.id.frgmContGame, visitedFragment);}
                else if (id == R.id.nav_radio){
                    if (radioFragment==null) radioFragment=new RadioFragment();
                    fTrans = fTrans.replace(R.id.frgmContGame, radioFragment);}
                else if (id == R.id.nav_leaderboard){
                    if (detailSpotFragment==null) detailSpotFragment=new DetailSpotFragment();
                    fTrans = fTrans.replace(R.id.frgmContGame, detailSpotFragment);}
                else if (id == R.id.nav_allpoints){
                    if (startGameFragment==null) startGameFragment=new StartGameFragment();
                    fTrans = fTrans.replace(R.id.frgmContGame, startGameFragment);}

            } else {
                if (loginFragment==null) loginFragment=new LoginFragment();
                fTrans = fTrans.replace(R.id.frgmContGame,loginFragment);
                Toast.makeText(this,"Для игры необходима авторизация",Toast.LENGTH_SHORT).show();
            }
        }else {
            if (selectCampFragment==null) selectCampFragment=new SelectCampFragment();
            fTrans = fTrans.replace(R.id.frgmContGame, selectCampFragment);
            Toast.makeText(this,"Для игры необходимо выбрать лагерь",Toast.LENGTH_SHORT).show();
        }
        fTrans.addToBackStack(null);
        fTrans.commit();





    }




public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == 0) {
        if (resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            visitedFragment.addAt(contents);
        } else if (resultCode == RESULT_CANCELED) {

            Toast toast = Toast.makeText(this, "Scan was Cancelled!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 25, 400);
            toast.show();
        }
    }
}

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_CONG) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            //adb.setTitle(R.string.cong + FILENAME);
            // сообщение
            adb.setMessage("Вы открыли еще одну точку и заработали 5 очков!");
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.game_drawer_layout);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
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
            selectCampFragment = new SelectCampFragment();
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmContGame, selectCampFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }
        if (id == R.id.endGame) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        select(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.game_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
