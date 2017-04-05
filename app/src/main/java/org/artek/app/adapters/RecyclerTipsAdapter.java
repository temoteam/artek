package org.artek.app.adapters;


import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.artek.app.Global;
import org.artek.app.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RecyclerTipsAdapter extends RecyclerView.Adapter<RecyclerTipsAdapter.ViewHolder> {

    public static List<HashMap<String,String>> data;


    private static Activity activity;
    private ImageLoader imageLoader;
    private static OkHttpClient client;

    public RecyclerTipsAdapter(List<HashMap<String,String>> data) {
        this.data = data;
    }

    public void init(ImageLoader imageLoader,Activity activity){
        this.imageLoader = imageLoader;
        client = new OkHttpClient();
        this.activity = activity;
    }

    @Override
    public RecyclerTipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_tip, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String,String> tip = data.get(position);
        holder.text.setText(tip.get("text"));
        holder.id = tip.get("id");
        if (tip.containsKey("name"))
            holder.name.setText(tip.get("name"));
        else
            holder.name.setText("NONAME");
        if (tip.containsKey("img"))
            imageLoader.displayImage(tip.get("img"),holder.ava);
        else
            holder.ava.setImageResource(R.drawable.artek);
        holder.ratingBar.setRating(Float.parseFloat(tip.get("rate")));
    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView text;
        public ImageView ava;
        public RatingBar ratingBar;
        public CardView cw;
        public RatingBar dialogRating;
        String id;



        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            text = (TextView) v.findViewById(R.id.text);
            ava = (ImageView) v.findViewById(R.id.ava);
            ratingBar = (RatingBar) v.findViewById(R.id.rete_bar);
            cw = (CardView) v.findViewById(R.id.cw);
            cw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRatingDialog();
                }
            });

        }

        public void showRatingDialog() {
            final AlertDialog.Builder ratingdialog = new AlertDialog.Builder(activity);

            ratingdialog.setTitle("Оценить совет");
            View linearlayout = activity.getLayoutInflater().inflate(R.layout.dialog_rating, null);
            dialogRating = (RatingBar) linearlayout.findViewById(R.id.ratingbar);
            ratingdialog.setView(linearlayout);
            ratingdialog.setMessage(text.getText());

            ratingdialog.setPositiveButton("Готово",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String url = Global.server+"artek/rate_tip.php?tipid="+id+"&rate="+(dialogRating.getRating()+"").replace(".",",")+"&uid="+Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_ID,null);
                            Log.i("kek",url);
                            final Request request = new Request.Builder().url(url).addHeader("Content-Type", "application/x-www-form-urlencoded").build();
                            client.newCall(request).enqueue(new okhttp3.Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {
                                                                    e.printStackTrace();
                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {


                                                                }
                                                            }

                            );
                            dialog.dismiss();
                        }
                    })

                    .setNegativeButton("Отмена",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

            ratingdialog.create();
            ratingdialog.show();
        }

    }

}
