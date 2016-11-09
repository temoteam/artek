package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.artek.app.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartGameFragment extends Fragment {


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
        return inflater.inflate(R.layout.fragment_start_game, null);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
        int score = Integer.parseInt(readFile("score"));
        int visitedAmmount =  score / 5;
        int all = 10;
        textView.setText("Посещенно точек: " + visitedAmmount + "\n"  +
                "Осталось точек: " + (all - visitedAmmount)+ "\n"  +
                "Колличество очков: " + score + "\n" +
                mayVisit());

    }
    public String mayVisit(){

        String kek = readFile("places");
        String topkek[] = kek.split("#");
        //Log.d("kekekeke",  String.valueOf(kek.indexOf(scan)));
        //Log.d("klololol",  scan);
        if ( Integer.parseInt(readFile("score")) == 0){

            return "Вы не посетили не одной точки!";
        }
        for (String v : topkek)
            if (!kek.contains(v)) {
                String azaza[] = readFile(v).split("#");
                String ret = "Предлагаем посетить точку " + azaza[0];
               return ret;
            }
        return "Вы посетили все точки!";
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
    public void onVisitedButtonClick(View view) {
        fTrans = getFragmentManager().beginTransaction();
        fTrans.replace(R.id.frgmCont, visitedFragment);
        fTrans.addToBackStack(null);
        fTrans.commit();
    }


}