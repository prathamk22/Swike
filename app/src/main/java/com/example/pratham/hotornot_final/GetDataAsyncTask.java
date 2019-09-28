package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.StackView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pratham.hotornot_final.WallActivitySwipe.NoPost;
import static com.example.pratham.hotornot_final.WallActivitySwipe.StackEmpty;
import static com.example.pratham.hotornot_final.WallActivitySwipe.baseAdapter;
import static com.example.pratham.hotornot_final.WallActivitySwipe.cardStackView;
import static com.example.pratham.hotornot_final.WallActivitySwipe.lottieAnimationView;
import static com.example.pratham.hotornot_final.WallActivitySwipe.postInfos;
import static com.example.pratham.hotornot_final.WallActivitySwipe.profileImages;
import static com.example.pratham.hotornot_final.WallActivitySwipe.swipeRefreshLayout;

public class GetDataAsyncTask extends AsyncTask<Void,Void,Void> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String bxs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private int match = 0;

    public GetDataAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SharedPreferences preferences = context.getSharedPreferences("loginCredentials", Context.MODE_PRIVATE);
        String username = preferences.getString ("email",null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0,a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                //Getting data from the firebase
                if (StackEmpty==1){
                    postInfos.clear();
                    DataSnapshot posts = dataSnapshot.child("Posts");
                    DataSnapshot inspect = dataSnapshot.child("User").child(bxs).child("Inspect");
                    DataSnapshot score = dataSnapshot.child("User").child(bxs).child("Score");
                    for(DataSnapshot postChild : posts.getChildren()){
                        match=0;
                        for (DataSnapshot inspectChild : inspect.getChildren()){
                            if(postChild.getKey().matches(inspectChild.getKey())){
                                match = 1;
                            }
                        }
                        if (match==0){
                            DataSnapshot url = postChild.child ("Url");
                            DataSnapshot name = postChild.child ("Name");
                            DataSnapshot username = postChild.child ("Username");
                            DataSnapshot date = postChild.child ("Date");
                            DataSnapshot likes = postChild.child("Likes");
                            DataSnapshot time = postChild.child("Time");
                            DataSnapshot dislikes = postChild.child("DisLikes");
                            PostInfo postInfo = new PostInfo (String.valueOf (url.getValue ())
                                    ,String.valueOf (name.getValue ())
                                    ,String.valueOf (username.getValue ())
                                    ,String.valueOf (date.getValue ())
                                    ,postChild.getKey()
                                    ,String.valueOf(likes.getValue())
                                    ,String.valueOf(dislikes.getValue())
                                    ,String.valueOf(time.getValue())
                                    ,String.valueOf(score.getValue()));
                            postInfos.add (postInfo);
                        }
                    }

                    //Arranging Swipe Deck according to score
                    ArrayList<String> scoreList = new ArrayList<>();
                    DataSnapshot user = dataSnapshot.child("User");
                    DataSnapshot postSize = dataSnapshot.child("Posts");
                    for (DataSnapshot postChild : postSize.getChildren()){
                        for (DataSnapshot userChild : user.getChildren()){
                            if(String.valueOf(postChild.child("Username").getValue()).matches(String.valueOf(userChild.child("Username").getValue()))){
                                DataSnapshot scoreValue = userChild.child("Score");
                                scoreList.add(String.valueOf(scoreValue.getValue()));
                                break;
                            }
                        }
                    }

                    for (DataSnapshot postChild : postSize.getChildren()){
                        for (DataSnapshot userChild : user.getChildren()){
                            if(String.valueOf(postChild.child("Username").getValue()).matches(String.valueOf(userChild.child("Username").getValue()))){
                                DataSnapshot photo = userChild.child("ProfilePhoto");
                                profileImages.add(String.valueOf(photo.getValue()));
                                break;
                            }
                        }
                    }

                    for (int i=0; i<scoreList.size(); i++){
                        if (i==scoreList.size()-1)
                            break;
                        for (int j=i+1; j<scoreList.size(); j++){
                            double scoreDouble = Double.parseDouble(scoreList.get(i));
                            scoreDouble = Math.round(scoreDouble);
                            double scoreDoubleNext = Double.parseDouble(scoreList.get(j));
                            scoreDoubleNext = Math.round(scoreDoubleNext);
                            int scoreInt = (int) scoreDouble;
                            int scoreIntNext = (int) scoreDoubleNext;
                            if(scoreInt < scoreIntNext){
                                try{
                                    PostInfo temp = postInfos.get(i);
                                    postInfos.set(i, postInfos.get(j));
                                    postInfos.set(j, temp);
                                    scoreList.set(i, String.valueOf(scoreIntNext));
                                    scoreList.set(j, String.valueOf(scoreInt));

                                    String s = profileImages.get(i);
                                    profileImages.set(i, profileImages.get(j));
                                    profileImages.set(j,s);
                                }catch (Exception e){
                                }
                            }
                        }
                    }

                    if (postInfos.size()==0){
                        swipeRefreshLayout.setEnabled(true);
                        lottieAnimationView.setAnimation("error.json");
                        lottieAnimationView.loop(false);
                        lottieAnimationView.playAnimation();
                        NoPost.setVisibility(View.VISIBLE);
                        cancel(true);
                    }else {
                        baseAdapter.notifyDataSetChanged();
                        lottieAnimationView.pauseAnimation();
                        lottieAnimationView.setVisibility(View.INVISIBLE);
                        cardStackView.animate().alphaBy(1f).setDuration(1000);
                        StackEmpty=0;
                        cancel(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText (context, "Error 411: " + databaseError.getMessage (), Toast.LENGTH_SHORT).show ();
                cancel(true);
            }
        });
        return null;
    }
}