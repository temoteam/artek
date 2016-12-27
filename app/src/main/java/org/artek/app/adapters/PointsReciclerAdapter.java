package org.artek.app.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.artek.app.R;

import java.util.ArrayList;


public class PointsReciclerAdapter extends RecyclerView.Adapter<PointsReciclerAdapter.ViewHolder> {

    private ArrayList<String> mDataset;

    public PointsReciclerAdapter(ArrayList<String> dataset) {
        mDataset = dataset;
    }

    @Override
    public PointsReciclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recicler_point_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

       holder.title.setText(mDataset.get(position));



    }

    @Override
    public int getItemCount() {
        return mDataset.size();
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


