package ru.temoteam.artek.app.main;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ru.temoteam.artek.app.ExceptionHandler;


public class NoInternetFragment extends ArtekFragment implements View.OnClickListener {

    ArtekFragment from;
    Button reload;


    public NoInternetFragment() {
        // Required empty public constructor
    }

    public void setFrom(ArtekFragment from) {
        this.from = from;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        View result = inflater.inflate(ru.temoteam.artek.app.R.layout.fragment_no_internet, container, false);
        reload = (Button) result.findViewById(ru.temoteam.artek.app.R.id.reload);
        reload.setOnClickListener(this);
        super.init(false);
        return result;
    }

    @Override
    public void onClick(View view) {
        if (from != null)
            getActivity().getFragmentManager().beginTransaction().replace(ru.temoteam.artek.app.R.id.frgmCont, from).commit();
    }
}
