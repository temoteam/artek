package org.artek.app.account;



import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.artek.app.ExceptionHandler;
import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.main.ArtekFragment;
import org.artek.app.main.NewsFragment;

public class LoginVKFragment extends ArtekFragment {



    public LoginVKFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        super.init(false);
        return inflater.inflate(R.layout.fragment_login_vk, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        WebView loginVkWeb = (WebView) getView().findViewById(R.id.webView);
        loginVkWeb.setWebViewClient(new VkWebViewClient());
        loginVkWeb.getSettings().setJavaScriptEnabled(true);
        loginVkWeb.loadUrl("https://oauth.vk.com/authorize?client_id=5376108&display=mobile&scope=friends,photos,audio,video,status,notes,wall,groups,messages,offline&redirect_uri=https://oauth.vk.com/blank.html&response_type=token&v=5.59");
    }

    public void getUserData(String url) {
        int a = url.indexOf("access_token");
        int b = url.indexOf("&expires_in");
        String token = url.substring(a + 13, b);
        a = url.indexOf("user_id=");
        String user_id = url.substring(a + 8);
        SharedPreferences sPref = Global.sharedPreferences;
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Global.SharedPreferencesTags.LAST_TOKEN, token);
        ed.putString(Global.SharedPreferencesTags.LAST_ID, user_id);
        ed.apply();

        Global.accountManager.login(token,user_id);





        getFragmentManager().beginTransaction().replace(R.id.frgmCont, new NewsFragment()).commit();
    }


    private class VkWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("https://oauth.vk.com/blank.html") & (!url.contains("error"))) {
                getUserData(url);
                return true;
            } else if (url.contains("error")) {
                return false;
            } else {
                view.loadUrl(url);
                return true;
            }
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (url.contains("error")) {
                return;
            } else if (url.contains("access_token")) {
                return;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
}
