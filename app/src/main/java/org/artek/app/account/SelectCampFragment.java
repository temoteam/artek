package org.artek.app.account;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.artek.app.adapters.Camps;
import org.artek.app.R;
import org.artek.app.adapters.CampsAdapter;

public class SelectCampFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_camp, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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

        CampsAdapter adapter = new CampsAdapter(getContext(),
                R.layout.listview_enter_camp, camps_data);

        ListView listView1 = (ListView) getActivity().findViewById(R.id.campsview);

        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch ((int) id) {
                    case 0:
                        getActivity().getApplication().setTheme(R.style.AppThemeMorskoy);
                        break;
                    case 1:
                        getActivity().getApplication().setTheme(R.style.AppThemeAlmazny);
                        break;
                    case 2:
                        getActivity().getApplication().setTheme(R.style.AppThemeChrustalny);
                        break;
                    case 3:
                        getActivity().getApplication().setTheme(R.style.AppThemeKiparis);
                        break;
                    case 4:
                        getActivity().getApplication().setTheme(R.style.AppThemeLazyrny);
                        break;
                    case 5:
                        getActivity().getApplication().setTheme(R.style.AppThemeLes);
                        break;
                    case 6:
                        getActivity().getApplication().setTheme(R.style.AppThemeMorflot);
                        break;
                    case 7:
                        getActivity().getApplication().setTheme(R.style.AppThemeOzer);
                        break;
                    case 8:
                        getActivity().getApplication().setTheme(R.style.AppThemePole);
                        break;
                    case 9:
                        getActivity().getApplication().setTheme(R.style.AppThemeReka);
                        Log.d("*", " id reka = "
                                + id);
                        break;
                    case 10:
                        getActivity().getApplication().setTheme(R.style.AppThemeYantar);
                        break;
                }


                getFragmentManager().beginTransaction().replace(R.id.frgmCont, new LoginFragment()).commit();


                Log.d("*", "itemClick: position = " + position + ", id = "
                        + id);
            }
        });

    }



}