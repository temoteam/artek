package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;


public class MessageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        return inflater.inflate(R.layout.fragment_message, null);

    }
}
