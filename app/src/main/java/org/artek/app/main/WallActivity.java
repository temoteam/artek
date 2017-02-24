package org.artek.app.main;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.adapters.RecyclerCommentsAdapter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WallActivity extends AppCompatActivity {

    private Bundle data;

    private Activity activity;

    private static Animation click;

    private static OkHttpClient client;

    private ImageView img;
    private ImageView like;
    private ImageView repost;
    private TextView likes;
    private TextView reposts;
    private TextView text;
    private RecyclerView comments;

    private String wallId;
    private String ownerId;

    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        img = (ImageView) findViewById(R.id.img);
        like = (ImageView) findViewById(R.id.imageView2);
        repost = (ImageView) findViewById(R.id.imageView3);
        text = (TextView) findViewById(R.id.description);
        likes = (TextView) findViewById(R.id.likes);
        reposts = (TextView) findViewById(R.id.reposts);
        comments = (RecyclerView) findViewById(R.id.comments);
        comments.setLayoutManager(new LinearLayoutManager(this));

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        client = new OkHttpClient();
        click = AnimationUtils.loadAnimation(this, R.anim.click);

        data = getIntent().getExtras();
        wallId = data.getString("wallid");
        ownerId = data.getString("ownerid");

        imageLoader.displayImage(data.getString("img"), img);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.startAnimation(click);
                String url = "https://api.vk.com/method/likes.add?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&item_id=" + wallId + "&owner_id="+ownerId+"&type=post";
                final Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String message = response.body().string();
                                                        if (message.contains("{\"response\":{\"likes\"")) {
                                                            likes.setText("" + (Integer.parseInt(likes.getText().toString()) + 1));
                                                        }

                                                    }
                                                }

                );
            }
        });
        repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repost.startAnimation(click);
                String url = "https://api.vk.com/method/wall.repost?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&object=wall"+ownerId+"_" + wallId;
                final Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        String message = response.body().string();
                                                        Log.i(request.toString(), message);
                                                        if (message.contains("{\"response\":{\"success")) {
                                                            reposts.setText("" + (Integer.parseInt(reposts.getText().toString()) + 1));
                                                        }

                                                    }
                                                }

                );
            }
        });

        likes.setText(data.getString("likes"));
        reposts.setText(data.getString("reposts"));
        text.setText(data.getString("text"));

        activity = this;

        new LoadComments().execute();
    }

    private class LoadComments extends AsyncTask<Void,Void,RecyclerCommentsAdapter>{

        @Override
        protected RecyclerCommentsAdapter doInBackground(Void... params) {
            try {
                List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
                List<NameValuePair> pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("v","5.62"));
                pairs.add(new BasicNameValuePair("post_id",wallId));
                pairs.add(new BasicNameValuePair("owner_id",ownerId));
                pairs.add(new BasicNameValuePair("count","100"));
                JSONArray jArrayComments = new JSONObject(getQuery(pairs,new URL("https://api.vk.com/method/wall.getComments"))).getJSONObject("response").getJSONArray("items");
                String users = "";

                for (int i = 0;i<jArrayComments.length();i++){
                    JSONObject jComment = jArrayComments.getJSONObject(i);
                    HashMap<String,String> comment = new HashMap<String,String>();
                    String text = jComment.getString("text");
                    if (text.contains("[")&&text.contains("|")&&text.contains("]"))
                        text = text.substring(text.indexOf("|")+1).replace("]","");

                    comment.put("text",text);
                    comment.put("id",jComment.getString("from_id"));
                    if (users.equals(""))
                        users = jComment.getString("from_id");
                    else
                        users = users+", "+jComment.getString("from_id");
                    data.add(comment);
                }

                pairs = new ArrayList<>();
                pairs.add(new BasicNameValuePair("v","5.62"));
                pairs.add(new BasicNameValuePair("fields","photo_200"));
                pairs.add(new BasicNameValuePair("user_ids",users));

                JSONArray jArrayUsers = new JSONObject(getQuery(pairs,new URL("https://api.vk.com/method/users.get"))).getJSONArray("response");
                for (int i = 0;i<jArrayUsers.length();i++) {
                    JSONObject jUser = jArrayUsers.getJSONObject(i);
                    HashMap<String, String> comment = data.get(i);
                    comment.put("name", jUser.getString("first_name") + " " + jUser.getString("last_name"));
                    comment.put("img", jUser.getString("photo_200"));
                }

                return new RecyclerCommentsAdapter(data);

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }


        private String getQuery(List<NameValuePair> params,URL url) throws Exception{
            StringBuilder result = new StringBuilder();
            boolean first = true;

            for (NameValuePair pair : params)
            {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
            }

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(result.toString());
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            Scanner in = new Scanner(conn.getInputStream());
            String res = "";
            while (in.hasNextLine()){
                if (res.equals("")) res = in.nextLine();
                else res = res + " " + in.nextLine();
            }
            return res;
        }

        @Override
        protected void onPostExecute(RecyclerCommentsAdapter recyclerCommentsAdapter) {
            super.onPostExecute(recyclerCommentsAdapter);
            recyclerCommentsAdapter.init(imageLoader);
            comments.setAdapter(recyclerCommentsAdapter);
        }
    }
}
