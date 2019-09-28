package com.example.pratham.hotornot_final;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.pratham.hotornot_final.WallActivitySwipe.postInfos;

public class ImageRoller extends AppCompatActivity {

    String value,url;
    ImageView ImageView;
    TextView name,username;
    CircleImageView circleImageView;
    ProgressBar image,circle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_roller);
        value = getIntent().getStringExtra("value");
        url = getIntent().getStringExtra("profile");
        image = findViewById(R.id.image);
        circle = findViewById(R.id.circle);
        ImageView = findViewById(R.id.ImageView);
        circleImageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        name.setTypeface(typeface);
        username.setTypeface(typeface);
        int valeuInt = Integer.parseInt(value);
        Glide.with(getApplicationContext()).load(postInfos.get(valeuInt).Url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                circle.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(ImageView);
        Glide.with(getApplicationContext()).load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                image.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(circleImageView);
        name.setText(postInfos.get(valeuInt).name);
        username.setText("@"+postInfos.get(valeuInt).username);
    }
}
