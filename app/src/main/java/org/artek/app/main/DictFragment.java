package org.artek.app.main;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;

import java.util.ArrayList;
import java.util.HashMap;


public class DictFragment extends ArtekFragment {

    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_dict, null);
        list = (ListView) result.findViewById(R.id.list);
        return result;
    }

    private String name = "Dictionary";
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        super.onActivityCreated(savedInstanceState);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


        ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;


        map = new HashMap<String, String>();
        map.put("Name", "Аю-даг");
        map.put("Des", "Медведь-гора, один из символов Артека");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Адалары");
        map.put("Des", "Скалы, напротив лагеря \"Лазурный\"");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Суук-Су");
        map.put("Des", "Дворец искусств");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Костерок");
        map.put("Des", "Артековская эмблема");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "БТМО");
        map.put("Des", "Большие тацны маленького острова, знакомство с Артеком, через игру");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Костровая");
        map.put("Des", "Главная площадь каждого из лагерей Артека");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Международка");
        map.put("Des", "Международная смена");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Мор. отряд");
        map.put("Des", "Разновидность профильного отряда, специализированный в морском деле");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Тур. отряд");
        map.put("Des", "Разновидность профильного отряда, специализированный в туристическом деле");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Ночной фей");
        map.put("Des", "Ночной вожатый");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Артбол");
        map.put("Des", "Главная артековская спортивная игра");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Уголек");
        map.put("Des", "Уголек, который день увозят с собой на память от прощального костра");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Шаляпинка");
        map.put("Des", "Небольшой скалистый мыс у лагеря \"Лазурный\"");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "КМАТ");
        map.put("Des", "Конкурс массового артековского танца");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "Массовка");
        map.put("Des", "рвзновидность проведения дискотеки, вожатый в центре, дети в кругу");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "КТМ");
        map.put("Des", "Контрольный туристический маршрут");

        myArrList.add(map);

        map = new HashMap<String, String>();

        map.put("Name", "КТД");
        map.put("Des", "Коллективное творческое дело");

        myArrList.add(map);


        SimpleAdapter adapter = new SimpleAdapter(getActivity(), myArrList, android.R.layout.simple_list_item_2,
                new String[]{"Name", "Des"},
                new int[]{android.R.id.text1, android.R.id.text2});
        list.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        setRetainInstance(true);
    }


}