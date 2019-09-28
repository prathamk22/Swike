package com.example.pratham.hotornot_final;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InformationActivity extends AppCompatActivity {

    TextView name,likes,dislikes,date;
    ImageView imageView;
    LinearLayout linearLayout;
    private String bxs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<PostInfo> arrayList;
    RelativeLayout relativeLayout;
    int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        String integer = getIntent().getStringExtra("int");
        relativeLayout = findViewById(R.id.relative);
        linearLayout = findViewById(R.id.linear);
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())) {
            Toast.makeText(this, "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
        }
        relativeLayout.bringToFront();
        linearLayout.bringToFront();
        value = Integer.parseInt(integer);
        arrayList = new ArrayList();
        name = findViewById(R.id.name);
        likes = findViewById(R.id.likes);
        dislikes = findViewById(R.id.dislike);
        date = findViewById(R.id.date);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        name.setTypeface(typeface);
        likes.setTypeface(typeface);
        dislikes.setTypeface(typeface);
        date.setTypeface(typeface);
        imageView = findViewById(R.id.imageView);
        imageView.bringToFront();
        getData();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        linearLayout.setLayoutParams(params);
        colorChangelistner();
    }

    public void getData(){
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString("email", null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0, a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot post = dataSnapshot.child("Posts");
                DataSnapshot snapshot = dataSnapshot.child("User").child(bxs).child("Posts");
                for (DataSnapshot posts : snapshot.getChildren()){
                    for(DataSnapshot time:post.getChildren()){
                        if (time.getKey().equals(posts.getValue())){
                            DataSnapshot url = time.child("Url");
                            DataSnapshot name = time.child("Name");
                            DataSnapshot username = time.child("Username");
                            DataSnapshot date = time.child("Date");
                            DataSnapshot likes = time.child("Likes");
                            DataSnapshot dislikes = time.child("DisLikes");
                            PostInfo postInfo = new PostInfo(String.valueOf(url.getValue()),
                                    String.valueOf(name.getValue()),
                                    String.valueOf(username.getValue()),
                                    String.valueOf(date.getValue()),
                                    String.valueOf(time.getKey()),
                                    String.valueOf(likes.getValue()),
                                    String.valueOf(dislikes.getValue()));
                            arrayList.add(postInfo);
                        }
                    }
                }
                Glide.with(getApplicationContext()).load(arrayList.get(value).Url).into(imageView);
                name.setText(arrayList.get(value).name);
                date.setText(arrayList.get(value).date);
                likes.setText(arrayList.get(value).likes);
                dislikes.setText(arrayList.get(value).dislikes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void colorChangelistner(){
        new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                int colorStart = getResources().getColor(android.R.color.white);
                int colorEnd = getResources().getColor(android.R.color.black);
                final ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorStart,colorEnd);
                animator.setDuration(250);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        relativeLayout.setBackgroundColor((int) animation.getAnimatedValue());
                    }
                });
                animator.start();
            }
        }.start();
    }
}
