package org.artek.app.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.artek.app.R;


public class ArtekDetiFragment extends Fragment {

    WebView wv;

    public ArtekDetiFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_artekdeti, container, false);
        wv = (WebView) v.findViewById(R.id.deti_wv);
        wv.loadUrl("http://xn--80akpwk.xn--d1acj3b/accountartekplus/login?ReturnUrl=%2F");

        // Enable Javascript
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);


        wv.setWebViewClient(new WebViewClient());

        return v;
    }

}
