package org.artek.app.main;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.artek.app.AnalyticsApplication;
import org.artek.app.ExceptionHandler;
import org.artek.app.R;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class NewsFragment extends Fragment {


    ListView mListView;
    String domain = "artekmedia";
    Context baseContext;
    NoInternetFragment noInternetFragment;
    private String name = "News";


    public NewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        setRetainInstance(true);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        Tracker mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Image~" + name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

    @Override
    public void onResume() {
        super.onResume();
        baseContext = getActivity().getBaseContext();

        noInternetFragment = new NoInternetFragment();
        noInternetFragment.setFrom(this);

        String strUrl = "https://api.vk.com/method/wall.get?domain=" + domain + "&count=50";
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(strUrl);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_blank, container, false);
        mListView = (ListView) result.findViewById(R.id.listView);
        return result;
    }


    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (iStream != null) {
                iStream.close();
            }
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        String data = null;

        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {

            ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
            listViewLoaderTask.execute(result);
        }
    }

    /**
     * AsyncTask для разбора JSON данных и загрузки в ListView
     */
    private class ListViewLoaderTask extends
            AsyncTask<String, Void, SimpleAdapter> {

        JSONObject jObject;

        @Override
        protected SimpleAdapter doInBackground(String... strJson) {
            try {
                jObject = new JSONObject(strJson[0]);
                NewsJSONParser newsJsonParser = new NewsJSONParser();
                newsJsonParser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            NewsJSONParser newsJsonParser = new NewsJSONParser();
            List<HashMap<String, Object>> countries = null;
            try {
                countries = newsJsonParser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String[] from = {"text", "imageLogo", "details"};
            int[] to = {R.id.textView, R.id.imageView, R.id.textView2};
            return new SimpleAdapter(baseContext,
                    countries, R.layout.item_news, from, to);
        }

        @Override
        protected void onPostExecute(SimpleAdapter adapter) {

            try {

                mListView.setAdapter(adapter);

                for (int i = 0; i < adapter.getCount(); i++) {
                    HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
                    String imgUrl = (String) hm.get("imageLogo_path");
                    ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
                    HashMap<String, Object> hmDownload = new HashMap<>();
                    hm.put("imageLogo_path", imgUrl);
                    hm.put("position", i);
                    imageLoaderTask.execute(hm);


                }
            } catch (NullPointerException e) {
                e.printStackTrace();

                getFragmentManager().beginTransaction().replace(R.id.frgmCont, noInternetFragment).commit();
                onPause();
                onStop();
            }
        }
    }


    private class ImageLoaderTask extends
            AsyncTask<HashMap<String, Object>, Void, HashMap<String, Object>> {

        @Override
        protected HashMap<String, Object> doInBackground(
                HashMap<String, Object>... hm) {

            InputStream iStream;
            String imgUrl = (String) hm[0].get("imageLogo_path");
            int position = (Integer) hm[0].get("position");

            URL url;
            try {
                url = new URL(imgUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                File cacheDirectory = baseContext.getCacheDir();
                File tmpFile = new File(cacheDirectory.getPath() + "/wpta_"
                        + position + ".png");
                FileOutputStream fOutStream = new FileOutputStream(tmpFile);
                Bitmap b = BitmapFactory.decodeStream(iStream);
                b.compress(Bitmap.CompressFormat.PNG, 100, fOutStream);
                fOutStream.flush();
                fOutStream.close();

                HashMap<String, Object> hmBitmap = new HashMap<>();
                hmBitmap.put("imageLogo", tmpFile.getPath());
                hmBitmap.put("position", position);
                return hmBitmap;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String, Object> result) {
            String path = (String) result.get("imageLogo");
            int position = (Integer) result.get("position");
            SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();
            if(position != 0){
            HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);
            hm.put("imageLogo", path);
            adapter.notifyDataSetChanged();}
        }
    }
}


