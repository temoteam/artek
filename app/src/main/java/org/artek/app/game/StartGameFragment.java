package org.artek.app.game;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartGameFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        return inflater.inflate(R.layout.fragment_start_game, null);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getApplicationContext().openFileInput(FILENAME)));

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