package org.artek.app.account;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.adapters.Camps;
import org.artek.app.R;
import org.artek.app.adapters.CampsAdapter;

public class SelectCampFragment extends Fragment {

    Global.appInterface appInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_camp, null);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAppInterface(Global.appInterface appInterface) {
        this.appInterface = appInterface;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        super.onActivityCreated(savedInstanceState);
        Camps camps_data[] = new Camps[]
                {
                        new Camps(R.drawable.morskoi, "Morskoy", "#002d62"),
                        new Camps(R.drawable.almazny, "Almazny", "#5bc8d8"),
                        new Camps(R.drawable.chrystalny, "Chrystalny", "#0055a5"),
                        new Camps(R.drawable.kiparisny, "Kiparisny", "#6d4a9e"),
                        new Camps(R.drawable.lazyrnoi, "Lazyrnoi", "#3dbd9a"),
                        new Camps(R.drawable.lesnoy, "Lesnoy", "#85c440"),
                        new Camps(R.drawable.morskayaflotiliya, "Morskaya flotiliya", "#4f91cd"),
                        new Camps(R.drawable.ozerniy, "Ozerniy", "#fdbf26"),
                        new Camps(R.drawable.polevoy, "Polevoy", "#f68c2b"),
                        new Camps(R.drawable.rechnoy, "Rechnoy", "#97c0e6"),
                        new Camps(R.drawable.yantarny, "Yantarny", "#ee2f2e")
                };

        CampsAdapter adapter = new CampsAdapter(getActivity(),
                R.layout.listview_enter_camp, camps_data);

        ListView listView1 = (ListView) getActivity().findViewById(R.id.campsview);

        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                setTheme((int)id,getActivity());
                if (appInterface!=null)
                appInterface.returner();
            }
        });

    }

    public void setTheme(int id, Activity activity){
        switch (id) {
            case 0:
                activity.setTheme(R.style.AppThemeMorskoy);
                break;
            case 1:
                activity.setTheme(R.style.AppThemeAlmazny);
                break;
            case 2:
                activity.setTheme(R.style.AppThemeChrustalny);
                break;
            case 3:
                activity.setTheme(R.style.AppThemeKiparis);
                break;
            case 4:
                activity.setTheme(R.style.AppThemeLazyrny);
                break;
            case 5:
                activity.setTheme(R.style.AppThemeLes);
                break;
            case 6:
                activity.setTheme(R.style.AppThemeMorflot);
                break;
            case 7:
                activity.setTheme(R.style.AppThemeOzer);
                break;
            case 8:
                activity.setTheme(R.style.AppThemePole);
                break;
            case 9:
                activity.setTheme(R.style.AppThemeReka);
                break;
            case 10:
                activity.setTheme(R.style.AppThemeYantar);
                break;
        }

        Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.CAMP,id).commit();

    }



}