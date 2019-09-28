package com.example.pratham.hotornot_final;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.pratham.hotornot_final.WallActivitySwipe.StackEmpty;
import static com.example.pratham.hotornot_final.WallActivitySwipe.baseAdapter;
import static com.example.pratham.hotornot_final.WallActivitySwipe.cardStackView;
import static com.example.pratham.hotornot_final.WallActivitySwipe.postInfos;
import static com.example.pratham.hotornot_final.WallActivitySwipe.swipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    android.support.v4.view.ViewPager ViewPager;
    Button Signup, Login;
    ImageView DirectLogin;
    RelativeLayout DirectLoginRelative,InDirectLoginRelative;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    SplashScreenPagerAdapter adapter;
    int Loaded = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView (R.layout.activity_main);
        DirectLogin = findViewById(R.id.DirectLogin);
        ViewPager = findViewById(R.id.ViewPager);
        Signup = findViewById(R.id.SignUp);
        Login = findViewById(R.id.Login);
        adapter = new SplashScreenPagerAdapter(getSupportFragmentManager(), 4);
        ViewPager.setAdapter(adapter);
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        DirectLoginRelative = findViewById(R.id.DirectLoginRelative);
        InDirectLoginRelative = findViewById(R.id.InDirectLoginRelative);
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString ("email",null);
        String password = preferences.getString ("passowrd",null);
        if (username != null && password != null) {
            if (!username.matches("") && !password.matches("")) {
                DirectLoginRelative.setVisibility(View.VISIBLE);
                InDirectLoginRelative.setVisibility(View.INVISIBLE);
                CountDownTimer count = new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent (MainActivity.this, WallActivity.class);
                        intent.putExtra ("directly","yes");
                        startActivity (intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        finish ();
                    }
                }.start();
            } else{
                DirectLoginRelative.setVisibility(View.INVISIBLE);
                InDirectLoginRelative.setVisibility(View.VISIBLE);
            }
        }else{
            DirectLoginRelative.setVisibility(View.INVISIBLE);
            InDirectLoginRelative.setVisibility(View.VISIBLE);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        ViewPager.setLayoutParams(params);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
