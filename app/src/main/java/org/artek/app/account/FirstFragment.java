package org.artek.app.account;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    FragmentTransaction fTrans;
    SelectCampFragment selectCampFragment;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);


    }

    public void onActivityCreated(Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        super.onActivityCreated(savedInstanceState);
        Log.d("kek", "first");


        selectCampFragment = new SelectCampFragment();


        writeFile("63", "Монумент «Дружба детей мира» #\n" +
                "\n" +
                "Установлен в июле 1967 года. \n" +
                "\n" +
                "Автор проекта Эрнст Иосифович Неизвестный, московский скульптор-монументалист, работающий в стиле модерн. \n" +
                "\n" +
                "Это скульптурная композиция монументального искусства. \n" +
                "\n" +
                "Скульптурная композиция была выполнена по заказу ЦК ВЛКСМ, перевезена из Москвы в «Артек» и смонтирована под руководством московского архитектора Анатолия Трофимовича Полянского и художников Хаима Рысина и Дениса Бодниекса. \n" +
                "\n" +
                "Динамичная, выразительная скульптурная композиция выполнена на полуовальной стене из розового армянского туфа с горельефными изображениями лиц детей разных национальностей. Лица детей и ладонь символизируют единство и дружбу детей разных стран мира.");
        writeFile("70", "Площадь Дружбы #\n" +
                "Фото 1972 г. Открыта 12 июля 1961 года.\n" +
                "\n" +
                "Автор проекта: Анатолий Трофимовича Полянский, московский архитектор. Длина 27 метров, ширина 26 метров, высота 8 метров.");


        // writeFile("","");

        writeFile("score", "0");
        writeFile("all", "10");
        writeFile("visited", "");
        getFragmentManager().beginTransaction().replace(R.id.frgmCont, new SelectCampFragment()).commit();
    }


    public void writeFile(String FILENAME, String content) {
        try {

            // open stream to write data
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().getApplicationContext().openFileOutput(FILENAME, MODE_PRIVATE)));

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
