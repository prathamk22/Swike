package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllPost extends AppCompatActivity {

    ListView listView;
    private String bxs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    ArrayList<PostInfo> arrayList;
    AllPostAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_post);
        listView = findViewById(R.id.Listview);
        arrayList = new ArrayList<>();
        getData();
        adapter = new AllPostAdapter(AllPost.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        listView.setLayoutParams(params);
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AllPost.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.profile_view_animation_reverse2, R.anim.profile_view_animation_reverse);
    }
}
