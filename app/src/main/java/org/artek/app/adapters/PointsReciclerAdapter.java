package org.artek.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.artek.app.R;

import java.net.URI;
import java.util.ArrayList;


public class PointsReciclerAdapter extends RecyclerView.Adapter<PointsReciclerAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> urls;
    private ArrayList<Boolean> complited;

    private ImageLoader imageLoader;




    public PointsReciclerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void init(ArrayList<String> titles,ArrayList<String> descriptions,ArrayList<String> urls,ArrayList<Boolean> complited){
        this.titles = titles;
        this.descriptions = descriptions;
        this.urls = urls;
        this.complited = complited;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(activity));

    }

    @Override
    public PointsReciclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recicler_point_item, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        String minDescription = descriptions.get(position);
        if (minDescription.length()>210)
            minDescription = minDescription.substring(0,200)+"...";
        holder.description.setText(minDescription);
        imageLoader.displayImage(urls.get(position), holder.pic);
        holder.detail.setText("");
        if (complited.get(position))
            holder.cardView.setBackgroundColor(Color.parseColor("#CFF9B2"));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        TextView description;
        TextView detail;
        ImageView pic;
        CardView cardView;

        ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            detail = (TextView) v.findViewById(R.id.detail);
            pic=(ImageView) v.findViewById(R.id.logo);
            cardView = (CardView) v.findViewById(R.id.card_view);
        }
    }


    }



