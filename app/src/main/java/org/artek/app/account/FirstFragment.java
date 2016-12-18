package org.artek.app.account;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.artek.app.ExceptionHandler;
import org.artek.app.FileRW;
import org.artek.app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    FragmentTransaction fTrans;
    SelectCampFragment selectCampFragment;

    private FileRW fileRW;
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

        fileRW = new FileRW(getActivity());
        super.onActivityCreated(savedInstanceState);
        fileRW.writeFile("first", "1");
        Log.d("kek", "first");


        selectCampFragment = new SelectCampFragment();


        fileRW.writeFile("63", "Монумент «Дружба детей мира» #\n" +
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
        fileRW.writeFile("70", "Площадь Дружбы #\n" +
                "Фото 1972 г. Открыта 12 июля 1961 года.\n" +
                "\n" +
                "Автор проекта: Анатолий Трофимовича Полянский, московский архитектор. Длина 27 метров, ширина 26 метров, высота 8 метров.");


        // fileRW.writeFile("","");

        fileRW.writeFile("score", "0");
        fileRW.writeFile("all", "10");
        fileRW.writeFile("visited", "");
        fileRW.writeFile("theme", "" + R.style.AppThemeYantar_NoActionBar);
        //Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.CAMP, R.style.AppThemeMorskoy_NoActionBar).commit();
        getFragmentManager().beginTransaction().replace(R.id.frgmCont, new SelectCampFragment()).commit();
    }


}
