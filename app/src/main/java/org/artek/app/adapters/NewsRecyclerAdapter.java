package org.artek.app.adapters;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.artek.app.Global;
import org.artek.app.R;
import org.artek.app.main.NewsFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tyrantgit.explosionfield.ExplosionField;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private List<HashMap<String, String>> countries ;
    private ImageLoader imageLoader;
    private Activity activity;
    private static Animation click;
    private static OkHttpClient client;

    final int minColor = 200;
    final int maxColor = 255;

    public NewsRecyclerAdapter(List<HashMap<String, String>> countries, Activity activity) {
        this.countries = countries;
        this.activity = activity;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        client= new OkHttpClient();
        click = AnimationUtils.loadAnimation(activity, R.anim.click);

    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_news_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> content = countries.get(position);

        holder.wallid = content.get("id");
        holder.position = position;

        String text = content.get("text");
        if (text.length()>150)
            text = text.substring(0,150)+"...";
        holder.text.setText(text);
        holder.likes.setText(content.get("likes"));
        holder.reposts.setText(content.get("reposts"));

        holder.cw.setBackgroundColor(Color.rgb(minColor + (int)(Math.random() * ((maxColor - minColor) + 1)),minColor + (int)(Math.random() * ((maxColor - minColor) + 1)),minColor + (int)(Math.random() * ((maxColor - minColor) + 1))));

        imageLoader.displayImage(content.get("img"), holder.logo);

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public String wallid;
        public int position;
        public CardView cw;
        public ImageView logo;
        public TextView text;
        public TextView likes;
        public TextView reposts;
        public ImageView like;
        public ImageView repost;



        public ViewHolder(View v) {
            super(v);
            cw = (CardView) v.findViewById(R.id.cw);

            logo = (ImageView) v.findViewById(R.id.img);
            like = (ImageView) v.findViewById(R.id.imageView2);
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    like.startAnimation(click);
                    String url = "https://api.vk.com/method/likes.add?access_token="+ Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null)+"&v=5.62&item_id="+wallid+"&owner_id=-44235988&type=post";
                    final Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String message = response.body().string();
                                                            if (message.contains("{\"response\":{\"likes\"")){
                                                                likes.setText(""+(Integer.parseInt(likes.getText().toString())+1));
                                                            }

                                                        }
                                                    }

                    );
                }
            });
            repost = (ImageView) v.findViewById(R.id.imageView3);
            repost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    repost.startAnimation(click);
                    String url = "https://api.vk.com/method/wall.repost?access_token="+ Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN,null)+"&v=5.62&object=wall-44235988_"+wallid;
                    final Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                    client.newCall(request).enqueue(new okhttp3.Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            e.printStackTrace();
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String message = response.body().string();
                                                            Log.i(request.toString(),message);
                                                            if (message.contains("{\"response\":{\"success")){
                                                                reposts.setText(""+(Integer.parseInt(reposts.getText().toString())+1));
                                                            }

                                                        }
                                                    }

                    );
                }
            });
            text = (TextView) v.findViewById(R.id.description);
            likes = (TextView) v.findViewById(R.id.likes);
            reposts = (TextView) v.findViewById(R.id.reposts);
        }
    }

}
