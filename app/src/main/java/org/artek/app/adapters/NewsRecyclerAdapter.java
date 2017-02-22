package org.artek.app.adapters;


import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.artek.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {

    private List<HashMap<String, String>> countries ;
    private ImageLoader imageLoader;
    private Activity activity;

    public NewsRecyclerAdapter(List<HashMap<String, String>> countries, Activity activity) {
        this.countries = countries;
        this.activity = activity;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));
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

        String wallid;




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
            repost = (ImageView) v.findViewById(R.id.imageView3);
            text = (TextView) v.findViewById(R.id.description);
            likes = (TextView) v.findViewById(R.id.likes);
            reposts = (TextView) v.findViewById(R.id.reposts);
        }
    }

}
