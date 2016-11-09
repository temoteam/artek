package org.artek.app.account;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.artek.app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView login_vk = (ImageView) getView().findViewById(R.id.imageButtonLoginVk);
        login_vk.setOnClickListener(loginVK);

        ImageView open_artek = (ImageView) getView().findViewById(R.id.imageLogoOpenArtek);
        open_artek.setOnClickListener(openArtek);

    }

    View.OnClickListener loginVK = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager().beginTransaction().replace(R.id.frgmCont, new LoginVKFragment()).addToBackStack(null).commit();
        }
    };

    View.OnClickListener openArtek = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.artek.org"));
            startActivity(browserIntent);
        }
    };


    private class SendPost extends AsyncTask<String, String, String> {
        public String answerHTTP;
        public String adress;
        public ProgressDialog mProgressDialog;
        public List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();



        SendPost(String adress) {
            this.adress = adress;
        }

        public void setParameters(String name, String value) {
            nameValuePairs.add(new BasicNameValuePair(name, value));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://dfd40d03.compilers.sphere-engine.com/api/v3/submissions?access_token=d98bcd9f1b408f40113023fcd2135dd4");
            HttpPost httppost = new HttpPost(adress);
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    answerHTTP = EntityUtils.toString(entity);
                }
            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }


    }

}
