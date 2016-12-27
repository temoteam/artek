package org.artek.app.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


import org.artek.app.R;

import java.util.ArrayList;


public class PointsReciclerAdapter extends RecyclerView.Adapter<PointsReciclerAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<String> urls;

    Activity activity;



    public PointsReciclerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void init(ArrayList<String> titles,ArrayList<String> descriptions,ArrayList<String> urls){
        this.titles = titles;
        this.descriptions = descriptions;
        this.urls = urls;


    }

    @Override
    public PointsReciclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recicler_point_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        String minDescription = descriptions.get(position);
        if (minDescription.length()>210)
            minDescription = minDescription.substring(0,200)+"...";
        holder.description.setText(minDescription);
        Picasso.with(activity).load(urls.get(position)).into(holder.pic); //ссылка на ImageView
     //   holder.pic.setImageBitmap(logos.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public ImageView pic;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            pic=(ImageView) v.findViewById(R.id.logo);
        }
    }


    }



