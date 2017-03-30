package org.artek.app.adapters;


import android.util.Log;
import android.view.LayoutInflater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.artek.app.R;

import java.util.HashMap;
import java.util.List;


public class RecyclerTipsAdapter extends RecyclerView.Adapter<RecyclerTipsAdapter.ViewHolder> {

    private List<HashMap<String,String>> data;


    private ImageLoader imageLoader;

    public RecyclerTipsAdapter(List<HashMap<String,String>> data) {
        this.data = data;
    }

    public void init(ImageLoader imageLoader){
        this.imageLoader = imageLoader;
    }

    @Override
    public RecyclerTipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_tip, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String> comment = data.get(position);
        holder.text.setText(comment.get("text"));
        if (comment.containsKey("name"))
            holder.name.setText(comment.get("name"));
        else
            holder.name.setText("NONAME");
        if (comment.containsKey("img"))
            imageLoader.displayImage(comment.get("img"),holder.ava);
        else
            holder.ava.setImageResource(R.drawable.artek);
    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView text;
        public ImageView ava;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            text = (TextView) v.findViewById(R.id.text);
            ava = (ImageView) v.findViewById(R.id.ava);
        }
    }

}
