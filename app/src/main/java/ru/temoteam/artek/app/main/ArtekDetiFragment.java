package ru.temoteam.artek.app.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.temoteam.artek.app.R;


public class ArtekDetiFragment extends ArtekFragment {

    WebView wv;

    public ArtekDetiFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.init(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog = dialog
                .setTitle("Тестовый функционал")
                .setMessage("В данный момент мы активно работает над добалением этого функционала в приложения, ожидайте обновлений")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

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
