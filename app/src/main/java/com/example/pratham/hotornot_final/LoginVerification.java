package com.example.pratham.hotornot_final;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginVerification extends AppCompatActivity {

    private FirebaseUser auth;
    LottieAnimationView verificationHolder;
    Button verify;
    Toolbar toolbar;
    ImageView back;
    TextView alreadyVerified;
    SwipeRefreshLayout swipetoRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_verification);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        back = findViewById(R.id.back);
        toolbar = findViewById(R.id.actionBar);
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())) {
            Toast.makeText(this, "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
        }
        Animation animation = AnimationUtils.loadAnimation(LoginVerification.this,R.anim.verification_anim);
        verificationHolder = findViewById(R.id.verificationHolder);
        alreadyVerified = findViewById(R.id.alreadyVerified);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        verify = findViewById(R.id.verify);
        alreadyVerified.setTypeface(typeface);
        alreadyVerified.startAnimation(animation);
        swipetoRefresh = findViewById(R.id.swipetoRefresh);
        swipetoRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                verification();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.login_animation,R.anim.login_animation);
            }
        });
        verification();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
    }

    public void verification(){
        if (auth != null && auth.isEmailVerified()) {
            verificationHolder.setAnimation("success_animation.json");
            verificationHolder.playAnimation();
            verify.setVisibility(View.INVISIBLE);
            swipetoRefresh.setRefreshing(false);
            alreadyVerified.setVisibility(View.VISIBLE);
        }else {
            verificationHolder.playAnimation();
            alreadyVerified.setVisibility(View.INVISIBLE);
            swipetoRefresh.setRefreshing(false);
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isComplete() || task.isSuccessful()){
                                Toast.makeText(LoginVerification.this, "Email Verification link sent to your Email Address", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginVerification.this, "Error occured " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.login_animation,R.anim.login_animation);
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
