package org.artek.app.main;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import org.artek.app.ExceptionHandler;
import org.artek.app.R;

public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, null);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        super.onActivityCreated(savedInstanceState);

        WebView wv = (WebView) getView().findViewById(R.id.mapWebView);
        wv.loadUrl("file:///android_asset/index.html");
        wv.getSettings().setBuiltInZoomControls(true);
        wv.addJavascriptInterface(new JsObject(), "Android");


    }

    @JavascriptInterface
    public void show(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    class JsObject {
        @JavascriptInterface
        public String toString() {
            return "Android";
        }
    }


}