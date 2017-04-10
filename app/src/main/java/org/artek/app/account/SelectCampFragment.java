package org.artek.app.account;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;

import org.artek.app.adapters.Camps;
import org.artek.app.adapters.CampsAdapter;
import org.artek.app.main.ArtekFragment;
import org.artek.app.main.MainActivity;

public class SelectCampFragment extends ArtekFragment {

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
        super.init(false);
    }

    public void setAppInterface(Global.appInterface appInterface) {
        this.appInterface = appInterface;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.frgmCont, new LoginFragment()).commit();
            }
        });
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

                wtiteTheme((int) id);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                if (appInterface!=null)
                    if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)){
                        Global.accountManager.getUserInfo(Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null));}
                else
                appInterface.returner();
            }
        });

    }

    public void wtiteTheme(int id) {
        switch (id) {
            case 0:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeMorskoy_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"morskoy").apply();
                break;
            case 1:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeAlmazny_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"almazniy").apply();
                break;
            case 2:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeChrustalny_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"hrustalniy").apply();
                break;
            case 3:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeKiparis_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"kiparisniy").apply();
                break;
            case 4:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeLazyrny_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"lazurniy").apply();
                break;
            case 5:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeLes_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"lesnoy").apply();
                break;
            case 6:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeMorflot_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"morskoy").apply();
                break;
            case 7:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID, R.style.AppThemeOzer_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"ozerniy").apply();
                break;
            case 8:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemePole_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"polwvoy").apply();
                break;
            case 9:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID, R.style.AppThemeReka_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"rechnoy").apply();
                break;
            case 10:
                Global.sharedPreferences.edit().putInt(Global.SharedPreferencesTags.THEME_ID,R.style.AppThemeYantar_NoActionBar).apply();
                Global.sharedPreferences.edit().putString(Global.SharedPreferencesTags.CAMP,"yantarniy").apply();
                break;
        }

    }


}