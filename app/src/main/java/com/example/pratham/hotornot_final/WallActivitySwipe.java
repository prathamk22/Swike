package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.VIBRATOR_SERVICE;

public class WallActivitySwipe extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static SackViewAdapter baseAdapter;
    ImageView circleImageView;
    public static LottieAnimationView lottieAnimationView;
    public static SwipeDeck cardStackView;
    public static TextView NoPost;
    ImageView cross,heart;
    int dislikes,likes;
    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout swipeRefreshLayout;
    Vibrator vibrator;
    public static ArrayList<PostInfo> postInfos;
    public static ArrayList<PostInfo> arranged;
    public static ArrayList<String> profileImages;
    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
    private String inspectString;
    RelativeLayout internet,YesInternet;
    public static int StackEmpty = 1;

    public WallActivitySwipe() {
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall_activity_swipe, null, false);
        //initializing
        circleImageView = view.findViewById(R.id.circle);
        profileImages = new ArrayList<>();
        lottieAnimationView = view.findViewById(R.id.lottiee);
        lottieAnimationView.loop(true);
        NoPost = view.findViewById(R.id.NoPost);
        lottieAnimationView.setAnimation("progress_bar.json");
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        cross = view.findViewById(R.id.cross);
        heart = view.findViewById(R.id.heart);
        postInfos = new ArrayList<> ();
        arranged = new ArrayList<> ();
        cardStackView = view.findViewById(R.id.cardView);
        SharedPreferences preferences = getActivity().getSharedPreferences("loginCredentials", Context.MODE_PRIVATE);
        String username = preferences.getString ("email",null);
        baseAdapter = new SackViewAdapter (getContext(), postInfos,profileImages);
        internet = view.findViewById(R.id.internet);
        YesInternet = view.findViewById(R.id.YesInternet);
        GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask(getActivity());
        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.popup);
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir.otf");

        ///Setting Function Values
        NoPost.setTypeface(typeface);
        swipeRefreshLayout.setNestedScrollingEnabled(true);
        swipeRefreshLayout.setEnabled(false);
        circleImageView.bringToFront();
        circleImageView.setDrawingCacheEnabled(true);
        cardStackView.setDrawingCacheEnabled (true);
        cardStackView.setAdapter(baseAdapter);
        getDataAsyncTask.execute();
        if(!InternetConnectionCheck.checkConnection(getContext())){
            YesInternet.setVisibility(View.INVISIBLE);
            internet.setVisibility(View.VISIBLE);
            lottieAnimationView.pauseAnimation();
        }else{
            YesInternet.setVisibility(View.VISIBLE);
            internet.setVisibility(View.INVISIBLE);
            YesInternet.setAnimation(animation);
        }
        int a;
        if (username != null) {
            a = username.indexOf("@");
            inspectString = username.substring(0,a);
        }
        cardStackView.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                try{
                    dislikes = Integer.parseInt(postInfos.get(position).dislikes);
                    dislikes++;
                    DatabaseReference references = reference.child("Posts").child(postInfos.get(position).UUId).child("DisLikes");
                    references.setValue(String.valueOf(dislikes));
                    String UUID = postInfos.get(position).UUId;
                    DatabaseReference inspect = reference.child("User").child(inspectString).child("Inspect").child(UUID);
                    inspect.setValue("DisLike");
                    Log.e("Postion", String.valueOf(position));
                    if (position==postInfos.size()-1){
                        StackEmpty = 1;
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cardSwipedRight(int position) {
                try{
                    likes = Integer.parseInt(postInfos.get(position).likes);
                    likes++;
                    DatabaseReference references = reference.child("Posts").child((postInfos.get(position).UUId)).child("Likes");
                    references.setValue(String.valueOf(likes));
                    IncreaseScore(position);
                    String UUID = postInfos.get(position).UUId;
                    DatabaseReference inspect = reference.child("User").child(inspectString).child("Inspect").child(UUID);
                    inspect.setValue("Like");
                    if (position==postInfos.size()-1){
                        StackEmpty = 1;
                    }
                }catch (Exception e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cardsDepleted() {
                swipeRefreshLayout.setEnabled(true);
                StackEmpty = 1;
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackView.swipeTopCardLeft(500);
                vibrator.vibrate(80);

            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStackView.swipeTopCardRight(500);
                vibrator.vibrate(80);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetDataAsyncTask getDataAsyncTask = new GetDataAsyncTask(getActivity());
                getDataAsyncTask.execute();
            }
        });

        return view;
    }

    public void IncreaseScore(final int i){
        try{
            double scoreInt = Double.parseDouble(postInfos.get(i).score);
            scoreInt = scoreInt + 0.5;
            DatabaseReference scoreRef = reference.child("User").child(inspectString).child("Score");
            scoreRef.setValue(String.valueOf(scoreInt));
        }catch (Exception e){

        }
    }
}
