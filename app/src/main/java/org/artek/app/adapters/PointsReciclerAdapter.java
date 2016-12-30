package org.artek.app.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
    private String detail;
    private ArrayList<String> urls;
    private ArrayList<Boolean> complited;

    Activity activity;



    public PointsReciclerAdapter(Activity activity) {
        this.activity = activity;
    }

    public void init(ArrayList<String> titles,ArrayList<String> descriptions,ArrayList<String> urls,ArrayList<Boolean> complited){
        this.titles = titles;
        this.descriptions = descriptions;
        this.urls = urls;
        this.complited = complited;


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
        Picasso.with(activity).load(urls.get(position)).into(holder.pic);
        holder.detail.setText("");

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView detail;
        public ImageView pic;
        public CardView cardView;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            detail = (TextView) v.findViewById(R.id.detail);
            pic=(ImageView) v.findViewById(R.id.logo);
            cardView = (CardView) v.findViewById(R.id.card_view);
        }
    }


    }



