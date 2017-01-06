package org.artek.app.game;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.artek.app.R;

public class PointActivity extends AppCompatActivity {

    TextView title;
    TextView description;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);

        Bundle data = getIntent().getExtras();

        title = (TextView)findViewById(R.id.title);
        description = (TextView)findViewById(R.id.description);
        logo = (ImageView) findViewById(R.id.logo);

        title.setText(data.getString("title"));
        description.setText(data.getString("description"));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        imageLoader.displayImage(data.getString("url"), logo);
    }
}
