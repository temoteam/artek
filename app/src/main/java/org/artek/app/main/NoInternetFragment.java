package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.artek.app.game.StartGameFragment;
import org.artek.app.game.VisitedFragment;


public class NoInternetFragment extends Fragment implements View.OnClickListener {

    Fragment from;
    Button reload;


    public NoInternetFragment() {
        // Required empty public constructor
    }

    public void setFrom(Fragment from) {
        this.from = from;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        View result = inflater.inflate(R.layout.fragment_no_internet, container, false);
        reload = (Button) result.findViewById(R.id.reload);
        reload.setOnClickListener(this);
        return result;
    }

    @Override
    public void onClick(View view) {
        if (from != null)
            getActivity().getFragmentManager().beginTransaction().replace(R.id.frgmCont, from).commit();
    }
}
