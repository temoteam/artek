package org.artek.app.account;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.artek.app.ExceptionHandler;

import org.artek.app.R;
import org.artek.app.Global;
import org.artek.app.main.NewsFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.MODE_WORLD_WRITEABLE;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginVKFragment extends Fragment {



    public LoginVKFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!(Thread.getDefaultUncaughtExceptionHandler() instanceof ExceptionHandler)) {
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_vk, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        /*
        CookieSyncManager.createInstance(getContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);*/


        WebView loginVkWeb = (WebView) getView().findViewById(R.id.webView);
        loginVkWeb.setWebViewClient(new VkWebViewClient());
        loginVkWeb.getSettings().setJavaScriptEnabled(true);
        WebSettings ws = loginVkWeb.getSettings();
        ws.setSaveFormData(false);
        ws.setSavePassword(false);
        loginVkWeb.loadUrl("https://oauth.vk.com/authorize?client_id=5376108&display=mobile&scope=friends,photos,audio,video,status,notes,wall,groups,messages,offline&redirect_uri=https://oauth.vk.com/blank.html&response_type=token&v=5.59");
    }

    public void getUserData(String url) {
        Log.e("handler", url);

        int a = url.indexOf("access_token");
        int b = url.indexOf("&expires_in");
        String token = url.substring(a + 13, b);
        a = url.indexOf("user_id=");
        String user_id = url.substring(a + 8);
        //Log.d("vk_auth",user_id);
        //Log.d("vk_auth", token);

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
            //Log.d(Constants.DEBUG_TAG, "Redirecting URL " + url);

            if (url.startsWith("https://oauth.vk.com/blank.html") & (!url.contains("error"))) {
                Log.d("vk_auth","url contains callback url");
                getUserData(url);
                return true;

            } else if (url.contains("error")) {
                return false;
            } else {
                Log.d("vk_auth","url not contains callback url");
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

    class httpGet extends AsyncTask<String, String, HashMap<String, String>> {
        String url, answerHTTP;


        public httpGet(String url) {
            this.url = url;
        }

        @Override
        protected HashMap<String, String> doInBackground(String... params) {

            HashMap<String, String> hashMap = new HashMap<String, String>();


            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            try {

                HttpResponse response = httpclient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    answerHTTP = EntityUtils.toString(entity);
                    try {
                        JSONObject jsonObject = new JSONObject(answerHTTP);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
/*
                        jsonObject = jsonArray.getJSONObject(0);
                        Log.i("Jsn",jsonObject.toString());
                        Log.i("Jsn",jsonArray.getString(0));
                        Log.i("Jsn",jsonArray.getString(2));
                        Log.i("Jsn",jsonArray.getString(1));
                        hashMap.put("first_name",jsonObject.getString("first_name"));
                        hashMap.put("last_name",jsonObject.getString("last_name"));
                        hashMap.put("uid",jsonObject.getString("uid"));

*/
                        return null;
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
