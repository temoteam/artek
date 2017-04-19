package ru.temoteam.artek.app.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import ru.temoteam.artek.app.R;


public class AboutFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about);
    }


}