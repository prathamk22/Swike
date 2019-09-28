package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class WallActivityProfileView extends Fragment {

    CircleImageView profileImage;
    TextView profileText, profileUsername,Score;
    LinearLayout settingsText,EditText;
    TextView dateText,likesText;
    ViewPager ProfileRecycler;
    LinearLayout lastPostclickable;
    CircleImageView lastImageview;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    RelativeLayout internetyes,internet,temp;
    String bxs = null, email;
    int position = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int i=0,dateAvailable=0;
    public WallActivityProfileView() {
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wall_activity_profile_view, container, false);
        ProfileRecycler = view.findViewById(R.id.ProfileRecycler);
        settingsText = view.findViewById(R.id.settingsText);
        temp = view.findViewById(R.id.temp);
        EditText = view.findViewById(R.id.EditText);
        dateText = view.findViewById(R.id.date);
        Score = view.findViewById(R.id.Score);
        likesText = view.findViewById(R.id.like);
        lastImageview = view.findViewById(R.id.lastImageView);
        lastPostclickable = view.findViewById(R.id.lastPostclickable);
        internet = view.findViewById(R.id.internet);
        internetyes = view.findViewById(R.id.internetyes);
        preferences = getContext().getSharedPreferences("ProfileData", MODE_PRIVATE);
        editor = preferences.edit();
        if (!InternetConnectionCheck.checkConnection(getActivity())) {
            Toast.makeText(getActivity(), "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
            internetyes.setVisibility(View.INVISIBLE);
            internet.setVisibility(View.VISIBLE);
        }else{
            internetyes.setVisibility(View.VISIBLE);
            internet.setVisibility(View.INVISIBLE);
        }
        lastPostclickable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dateText.getText().toString().matches(getString(R.string.no_post))){
                    Intent intent = new Intent(getActivity(),AllPost.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.profile_view_animation_2,R.anim.profile_view_animation);
                }

            }
        });
        ProfileRecyclerAdapter adapter = new ProfileRecyclerAdapter(getFragmentManager(),4);
        ProfileRecycler.setAdapter(adapter);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                ProfileRecycler.setCurrentItem(position,true);

                position++;
                if (position==4)
                    position=0;
                handler.postDelayed(this, 2500);
            }
        };
        runnable.run();
        profileUsername = view.findViewById(R.id.profileUsername);
        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });
        profileImage = view.findViewById(R.id.profileImage);
        profileImage.bringToFront();
        profileText = view.findViewById(R.id.profileName);
        profileText.bringToFront();
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/avenir.otf");
        profileText.setTypeface(typeface);
        Score.setTypeface(typeface);
        profileUsername.setTypeface(typeface);
        String name = preferences.getString("name",null);
        String username = preferences.getString("username",null);
        String score = preferences.getString("score",null);
        String lastdate = preferences.getString("lastDate",null);
        String lastlike = preferences.getString("lastLike",null);
        if(name!=null || username!=null || score!=null || lastdate!=null || lastlike!=null){
            profileText.setText(name);
            profileUsername.setText("@"+username);
            Score.setText(score);
            dateText.setText(lastdate);
            likesText.setText(lastlike);
        }
        getProfilePic();
        getData();
        return view;
    }

    public void getProfilePic() {
        SharedPreferences preferences = getActivity().getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString("email", null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0, a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                DataSnapshot snapshot = dataSnapshot.child("User").child(bxs).child("ProfilePhoto");
                Glide.with(getActivity()).load(String.valueOf(snapshot.getValue())).into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getData(){
        SharedPreferences preferences = getActivity().getSharedPreferences("loginCredentials", MODE_PRIVATE);
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
                if (getActivity() == null) {
                    return;
                }else{
                    DataSnapshot post = dataSnapshot.child("Posts");
                    DataSnapshot snapshot = dataSnapshot.child("User").child(bxs).child("Posts");
                    DataSnapshot name = dataSnapshot.child("User").child(bxs).child("Name");
                    DataSnapshot usernames = dataSnapshot.child("User").child(bxs).child("Username");
                    DataSnapshot DataScore = dataSnapshot.child("User").child(bxs).child("Score");
                    profileText.setText(String.valueOf(name.getValue()));
                    profileUsername.setText("@" + String.valueOf(usernames.getValue()));
                    Log.e("Score",String.valueOf(DataScore.getValue()));
                    double like = Double.parseDouble(String.valueOf(DataScore.getValue()));
                    like = Math.round(like);
                    int IntLikes = (int)like;
                    Score.setText(String.valueOf(IntLikes));
                    editor.putString("name", String.valueOf(name.getValue()));
                    editor.putString("username",String.valueOf(usernames.getValue()));
                    editor.putString("score", String.valueOf(IntLikes));
                    editor.commit();
                    editor.apply();
                    for (DataSnapshot posts : snapshot.getChildren()){
                        for(DataSnapshot time:post.getChildren()){
                            if (time.getKey().equals(posts.getValue())){
                                DataSnapshot url = time.child("Url");
                                DataSnapshot date = time.child("Date");
                                DataSnapshot likes = time.child("Likes");
                                Glide.with(getActivity()).load(String.valueOf(url.getValue())).into(lastImageview);
                                dateText.setText(String.valueOf(date.getValue()));
                                likesText.setText(String.valueOf(likes.getValue()));
                                editor.putString("lastDate", String.valueOf(date.getValue()));
                                editor.putString("lastLike",String.valueOf(likes.getValue()));
                                dateAvailable = 1;
                            }
                        }
                    }
                    if(dateAvailable==0){
                        dateText.setText(getString(R.string.no_post));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if(height > reqHeight || width > reqWidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;

            while ((halfHeight/inSampleSize)>= reqHeight &&(halfWidth/inSampleSize)>=reqWidth){
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSmapleBitmapfromRes(Resources res,int resId, int reqHeight,int reqWidth){
        final BitmapFactory.Options options= new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
