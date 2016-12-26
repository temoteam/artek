package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class StartGameFragment extends Fragment {

    private String name = "StartGameFrgmnt";
    FragmentTransaction fTrans;
    VisitedFragment visitedFragment;

    private View.OnClickListener mCorkyListener = new View.OnClickListener() {
        public void onClick(View v) {
            visitedFragment = new VisitedFragment();
            fTrans = getFragmentManager().beginTransaction();
            fTrans.replace(R.id.frgmContGame, visitedFragment);
            fTrans.addToBackStack(null);
            fTrans.commit();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        //writeFile("test1","");
        return inflater.inflate(R.layout.fragment_start_game, null);



    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


       // Button button1 = (Button) getActivity().findViewById(R.id.button31);
       // button1.setOnClickListener(mCorkyListener);

        writeText();

    }

    @Override
    public void onResume() {
        super.onResume();
        writeText();
    }

    public void writeText() {
        TextView textView = (TextView) getActivity().findViewById(R.id.textView2);
        int score = 0;
        if(readFile("score")!= "") {
            score = Integer.parseInt(readFile("score"));
        }
        int visitedAmmount =  score / 5;
        int all = 10;
        textView.setText(getString(R.string.visited_points) + visitedAmmount + "\n"  +
                getString(R.string.left_points) + (all - visitedAmmount)+ "\n"  +
                getString(R.string.score) + score + "\n" +
                mayVisit());

    }
    public String mayVisit(){

        String kek = readFile("places");
        String topkek[] = kek.split("#");
        if ( Integer.parseInt(readFile("score")) == 0){

            return getString(R.string.no_points);
        }
        for (String v : topkek)
            if (!kek.contains(v)) {
                String points[] = readFile(v).split("#");
                String ret = getString(R.string.let_visit) + points[0];
               return ret;
            }
        return getString(R.string.all_points);
    }
    public String readFile(String FILENAME) {
        String content = "";
        String str = "";
        try {

            // open stream to read data
            BufferedReader br = new BufferedReader(new InputStreamReader(getContext().openFileInput(FILENAME)));

            // read data
            while ((str = br.readLine()) != null) {
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

}