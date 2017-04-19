package ru.temoteam.artek.app.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.List;

import ru.temoteam.artek.app.R;


public class RecyclerCommentsAdapter extends RecyclerView.Adapter<RecyclerCommentsAdapter.ViewHolder> {

    private List<HashMap<String,String>> data;


    private ImageLoader imageLoader;

    public RecyclerCommentsAdapter(List<HashMap<String,String>> data) {
        this.data = data;
    }

    public void init(ImageLoader imageLoader){
        this.imageLoader = imageLoader;
    }

    @Override
    public RecyclerCommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_comment, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("filling","start");
        HashMap<String,String> comment = data.get(position);
        Log.i("map",comment.toString());
        holder.text.setText(comment.get("text"));
        Log.i("filling","settext");
        if (comment.containsKey("name"))
            holder.name.setText(comment.get("name"));
        else
            holder.name.setText("NONAME");
        Log.i("filling","tesname");
        if (comment.containsKey("img"))
            imageLoader.displayImage(comment.get("img"),holder.ava);
        else
            holder.ava.setImageResource(R.drawable.artek);
        Log.i("filling","setimg");
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
