package org.artek.app.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.artek.app.Global;
import org.artek.app.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WallActivity extends AppCompatActivity {

    private Bundle data;

    private static Animation click;

    private static OkHttpClient client;

    private ImageView img;
    private ImageView like;
    private ImageView repost;
    private TextView likes;
    private TextView reposts;
    private TextView text;
    private RecyclerView comments;

    private String wallId;
    private String ownerId;

    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        img = (ImageView) findViewById(R.id.img);
        like = (ImageView) findViewById(R.id.imageView2);
        repost = (ImageView) findViewById(R.id.imageView3);
        text = (TextView) findViewById(R.id.description);
        likes = (TextView) findViewById(R.id.likes);
        reposts = (TextView) findViewById(R.id.reposts);
        comments = (RecyclerView) findViewById(R.id.comments);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        client = new OkHttpClient();
        click = AnimationUtils.loadAnimation(this, R.anim.click);

        data = getIntent().getExtras();
        wallId = data.getString("wallid");
        ownerId = data.getString("ownerid");

        imageLoader.displayImage(data.getString("img"), img);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.startAnimation(click);
                String url = "https://api.vk.com/method/likes.add?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&item_id=" + wallId + "&owner_id="+ownerId+"&type=post";
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
                String url = "https://api.vk.com/method/wall.repost?access_token=" + Global.sharedPreferences.getString(Global.SharedPreferencesTags.LAST_TOKEN, null) + "&v=5.62&object=wall"+ownerId+"_" + wallId;
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

        likes.setText(data.getString("likes"));
        reposts.setText(data.getString("reposts"));
        text.setText(data.getString("text"));
    }
}
