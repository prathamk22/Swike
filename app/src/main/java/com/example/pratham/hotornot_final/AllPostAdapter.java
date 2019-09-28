package com.example.pratham.hotornot_final;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class AllPostAdapter extends ArrayAdapter{
    ArrayList<PostInfo> arrayList;
    Context context;
    LayoutInflater inflater;
    public AllPostAdapter(@NonNull Context context, int resource, ArrayList<PostInfo> arrayList) {
        super(context, resource);
        this.arrayList = arrayList;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }@Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
            view = inflater.inflate(R.layout.all_post_list_view,parent,false);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/avenir.otf");
            final ImageView imageView = view.findViewById(R.id.lastImageView);
            final TextView date = view.findViewById(R.id.date);
            final TextView like = view.findViewById(R.id.like);
            final TextView dislike = view.findViewById(R.id.disLikeText);
            Glide.with(context).load(arrayList.get(i).Url).into(imageView);
            date.setText(arrayList.get(i).date);
            dislike.setText(arrayList.get(i).dislikes);
            final int a = i;
            like.setText(arrayList.get(i).likes);
            date.setTypeface(typeface);
            like.setTypeface(typeface);
            dislike.setTypeface(typeface);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, InformationActivity.class);
                    intent.putExtra("int", String.valueOf(a));
                    Pair<View, String> pair = Pair.create((View)imageView, "imageView");
                    Pair<View, String> pair2 = Pair.create((View)date, "date");
                    Pair<View, String> pair3 = Pair.create((View)like, "like");
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)context,
                            pair,pair2,pair3);
                    context.startActivity(intent,options.toBundle());
                }
            });
        return view;
    }
}
