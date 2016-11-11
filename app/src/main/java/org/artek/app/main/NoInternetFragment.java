package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoInternetFragment extends Fragment implements View.OnClickListener {

    NewsFragment newsFragment;
    Button reload;


    public NoInternetFragment() {
        // Required empty public constructor
    }

    public void setNewsFragment(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        View result = inflater.inflate(R.layout.fragment_no_internet, container, false);
        reload = (Button) result.findViewById(R.id.reload);
        reload.setOnClickListener(this);
        return result;
    }

    @Override
    public void onClick(View view) {
        getActivity().getFragmentManager().beginTransaction().replace(R.id.frgmCont, newsFragment).commit();
    }
}
