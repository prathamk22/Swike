package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.joooonho.SelectableRoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.pratham.hotornot_final.WallActivitySwipe.StackEmpty;

public class SackViewAdapter extends BaseAdapter {
    public static ArrayList<PostInfo> postInfo;
    private ArrayList<String> profileImages;
    private Context context;
    private LayoutInflater inflater;

    public SackViewAdapter(@NonNull Context context, ArrayList<PostInfo> postInfo,ArrayList<String> profileImages) {
        this.context = context;
        this.postInfo = postInfo;
        this.profileImages = profileImages;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return postInfo.size ();
    }

    @Override
    public Object getItem(int position) {
        return postInfo.get (position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int i, View view, @NonNull ViewGroup parent) {
        view  = inflater.inflate (R.layout.card_sack_view, parent,false);
        final SelectableRoundedImageView imageView = view.findViewById (R.id.image_view);
        final ProgressBar progressBar = view.findViewById(R.id.progressBar);
        CircleImageView profileImage = view.findViewById(R.id.profileImage);
        imageView.setDrawingCacheQuality (View.DRAWING_CACHE_QUALITY_LOW);
        imageView.setDrawingCacheEnabled (true);
        imageView.setAlpha(0f);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/avenir.otf");
        final TextView name = view.findViewById (R.id.nameCards);
        final TextView username = view.findViewById (R.id.usernameCards);
        name.setText (postInfo.get (i).name);
        username.setText ("@" + postInfo.get (i).username);
        name.setTypeface(typeface);
        username.setTypeface(typeface);
        Glide.with(context).load(profileImages.get(i)).into(profileImage);
        Glide.with (context).load (postInfo.get (i).Url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setAlpha(0f);
                    imageView.setAlpha(1f);
                    return false;
                }
            }).into (imageView);
            name.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ImageRoller.class);
                    intent.putExtra("value",String.valueOf(i));
                    intent.putExtra("profile", profileImages.get(i));
                    Activity activity = (Activity) context;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, "enlarged");
                    context.startActivity(intent,compat.toBundle());
                }
            });
            username.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,ImageRoller.class);
                    intent.putExtra("value",String.valueOf(i));
                    intent.putExtra("profile", profileImages.get(i));
                    Activity activity = (Activity) context;
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, imageView, "enlarged");
                    context.startActivity(intent,compat.toBundle());
                }
            });
        return view;
    }
}