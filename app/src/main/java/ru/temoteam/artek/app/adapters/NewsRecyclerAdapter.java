package ru.temoteam.artek.app.adapters;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.temoteam.artek.app.Global;
import ru.temoteam.artek.app.main.WallActivity;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private static Activity activity;
    private static Animation click;
    private static OkHttpClient client;
    private List<HashMap<String, String>> countries;
    private ImageLoader imageLoader;


    public NewsRecyclerAdapter(List<HashMap<String, String>> countries, Activity activity) {
        this.countries = countries;
        NewsRecyclerAdapter.activity = activity;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
        client= new OkHttpClient();
        click = AnimationUtils.loadAnimation(activity, ru.temoteam.artek.app.R.anim.click);

    }

    @Override
    public NewsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(ru.temoteam.artek.app.R.layout.recycler_news_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> content = countries.get(position);

        holder.wallid = content.get("id");
        holder.position = position;
        holder.content = content;

        String text = content.get("text");
        if (text.length()>150)
            text = text.substring(0,150)+"...";
        holder.text.setText(text);
        holder.likes.setText(content.get("likes"));
        holder.reposts.setText(content.get("reposts"));

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
        HashMap<String, String> content;


        public ViewHolder(View v) {
            super(v);
            cw = (CardView) v.findViewById(ru.temoteam.artek.app.R.id.cw);

            logo = (ImageView) v.findViewById(ru.temoteam.artek.app.R.id.img);
            like = (ImageView) v.findViewById(ru.temoteam.artek.app.R.id.imageView2);

            repost = (ImageView) v.findViewById(ru.temoteam.artek.app.R.id.imageView3);
            if (Global.sharedPreferences.contains(Global.SharedPreferencesTags.LAST_TOKEN)) {
                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        like.startAnimation(click);
                        String url = "https://api.vk.com/method/likes.add?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&item_id=" + wallid + "&owner_id=-44235988&type=post";
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
                        String url = "https://api.vk.com/method/wall.repost?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&object=wall-44235988_" + wallid;
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
            } else {
                like.setEnabled(false);
                like.setClickable(false);
                like.setFocusable(false);
                repost.setEnabled(false);
                repost.setClickable(false);
                repost.setFocusable(false);
            }
            text = (TextView) v.findViewById(ru.temoteam.artek.app.R.id.description);
            likes = (TextView) v.findViewById(ru.temoteam.artek.app.R.id.likes);
            reposts = (TextView) v.findViewById(ru.temoteam.artek.app.R.id.reposts);

            View.OnClickListener open = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putString("wallid",wallid);
                    data.putString("ownerid","-44235988");
                    data.putString("img",content.get("img"));
                    data.putString("likes",content.get("likes"));
                    data.putString("reposts",content.get("reposts"));
                    data.putString("text",content.get("text"));
                    Intent intent = new Intent(activity, WallActivity.class);
                    intent.putExtras(data);
                    activity.startActivity(intent);
                }
            };

            logo.setOnClickListener(open);
            text.setOnClickListener(open);
        }
    }

}
