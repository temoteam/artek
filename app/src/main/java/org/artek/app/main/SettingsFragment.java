package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.artek.app.R;
import org.artek.app.account.FirstFragment;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button button = (Button) getActivity().findViewById(R.id.resetapp);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContext().deleteFile("first");
                getFragmentManager().beginTransaction().replace(R.id.frgmCont, new FirstFragment()).commit();
            }
        });

    }


}