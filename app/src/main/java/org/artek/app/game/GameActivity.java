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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.artek.app.main.MainActivity;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GameActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String FILENAME = "gameData";
    final String IO_LOG_TAG = "I/O_Logs";
    final int[] datass = {};
    int backButton = 0;
    int DIALOG_CONG = 11;
    String places = "63#70";

    FragmentTransaction fTrans;
    VisitedFragment visitedFragment;
    GameFragment gameFragment;
    org.artek.app.game.StartGameFragment StartGameFragment;
    org.artek.app.game.DetailSpotFragment DetailSpotFragment;

    private RecyclerView mRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Register the onClick listener with the implementation above


        writeFile("places", places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StartGameFragment = new StartGameFragment();
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.frgmContGame, StartGameFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                lala();
            }
        });

        //

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void lala() {
        Intent intent = new Intent(this, ScannerQRActivity.class);

        startActivityForResult(intent, 1);
    }


    public void onVisitedButtonClick(View view) {
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.frgmCont, visitedFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        TextView tw = (TextView) findViewById(R.id.textView2);
        String a = data.getSerializableExtra("data").toString();
        Integer id_building = 0;
        try {
            id_building = Integer.parseInt(a);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        Log.d("lala", id_building.toString() + " " + a);
        //tw.setText(a);
        getQr(a);
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
            return true;
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
        int id = item.getItemId();

        /*if (id == R.id.nav_game) {

            fTrans = getFragmentManager().beginTransaction();
            StartGameFragment = new StartGameFragment();
            fTrans.replace(R.id.frgmContGame, gameFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        } else*/ if (id == R.id.nav_visited) {
            fTrans = getFragmentManager().beginTransaction();
            visitedFragment = new VisitedFragment();
            fTrans.replace(R.id.frgmContGame, visitedFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        } else if (id == R.id.nav_leaderboard) {
            //
        } else if (id == R.id.nav_radio) {
            //
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void writeFile(String FILENAME, String content) {
        try {

            // open stream to write data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE)));

            // write data
            bw.write(content);

            // close stream
            bw.close();
            Log.d(IO_LOG_TAG, "File write");
            Log.d(IO_LOG_TAG, content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {
                Log.d(IO_LOG_TAG, str);
                content = content + str;
            }

            // close stream
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public void getQr(String scan) {
        String content = "";
        String str = "";

        String kek = readFile("places");
        String topkek = readFile("visited");
        //Log.d("kekekeke",  String.valueOf(kek.indexOf(scan)));
        //Log.d("klololol",  scan);
        if (topkek.contains(scan)) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Вы здесь уже были!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (kek.contains(scan)) {
                String azaz = readFile("visited");
                writeFile("visited", azaz + scan + "#");
                Integer score = Integer.parseInt(readFile("score"));
                score += 5;
                showDialog(DIALOG_CONG);
                deleteFile("score");
                writeFile("score", score.toString());

                String keklol[] = readFile(scan).split("#");
                writeFile("currcardclick", keklol[0]);
                fTrans = getFragmentManager().beginTransaction();
                DetailSpotFragment = new DetailSpotFragment();
                fTrans.replace(R.id.frgmContGame,DetailSpotFragment);
                fTrans.addToBackStack(null);
                fTrans.commit();

            } else {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Точка не найдена!", Toast.LENGTH_LONG);
                toast.show();
            }
        }
     /*   switch (scan){
            case 1:
                writeFile( "visited",  "");



    }*/
    }


}
