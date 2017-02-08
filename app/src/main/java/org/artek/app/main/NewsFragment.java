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

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class NewsFragment extends Fragment {


    private final OkHttpClient client = new OkHttpClient();
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
        downloadTask(strUrl);
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


    private void downloadTask(String uri) {
        Request request = new Request.Builder()
                .url(uri)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String message = response.body().string();
                                                ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
                                                listViewLoaderTask.execute(message);

                                            }
                                        }

        );
    }

    private void imageLoaderTask(HashMap<String, Object> hm) {

        String uri = (String) hm.get("imageLogo_path");
        final int position = (Integer) hm.get("position");

        final Request request = new Request.Builder()
                .url(uri)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        client.newCall(request).enqueue(new okhttp3.Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                InputStream iStream;
                                                iStream = response.body().byteStream();
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

                                                String path = (String) hmBitmap.get("imageLogo");
                                                int position = (Integer) hmBitmap.get("position");
                                                SimpleAdapter adapter = (SimpleAdapter) mListView.getAdapter();
                                                if (position != 0) {
                                                    HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(position);
                                                    hm.put("imageLogo", path);
                                                    adapter.notifyDataSetChanged();
                                                }


                                            }
                                        }

        );
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
                for (int i = 0; i < adapter.getCount(); i++) {
                    HashMap<String, Object> hm = (HashMap<String, Object>) adapter.getItem(i);
                    String imgUrl = (String) hm.get("imageLogo_path");
                    HashMap<String, Object> hmDownload = new HashMap<>();
                    hm.put("imageLogo_path", imgUrl);
                    hm.put("position", i);
                    imageLoaderTask(hm);


                }

                mListView.setAdapter(adapter);
            } catch (NullPointerException e) {
                e.printStackTrace();

                getFragmentManager().beginTransaction().replace(R.id.frgmCont, noInternetFragment).commit();
                onPause();
                onStop();
            }
        }
    }


}


