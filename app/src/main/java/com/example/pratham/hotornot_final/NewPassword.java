package com.example.pratham.hotornot_final;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

public class NewPassword extends AppCompatActivity {

    private FirebaseUser auth;
    MaterialEditText oldPassword,NewPassword,ConfirmNewPassword;
    Button ForgotPassword;
    ImageView back;
    TextView passwordString;
    LinearLayout NewPasswordHolder;
    Toolbar toolbar;
    int neww=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        back = findViewById(R.id.back);
        passwordString = findViewById(R.id.passwordString);
        toolbar = findViewById(R.id.actionBar);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.login_animation,R.anim.login_animation);
            }
        });
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())) {
            Toast.makeText(this, "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final String username = preferences.getString ("email",null);
        final String password = preferences.getString ("passowrd",null);
        oldPassword = findViewById(R.id.oldpassword);
        ForgotPassword = findViewById(R.id.ForgotPassword);
        NewPasswordHolder = findViewById(R.id.NewPasswordHolder);
        NewPassword = findViewById(R.id.NewPassword);
        ConfirmNewPassword = findViewById(R.id.ConfirmNewPassword);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        passwordString.setTypeface(typeface);
        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldPassword.getText().toString().matches(password) && neww==0){
                    oldPassword.setVisibility(View.INVISIBLE);
                    NewPasswordHolder.setVisibility(View.VISIBLE);
                    passwordString.setVisibility(View.INVISIBLE);
                    ForgotPassword.setText(R.string.new_password);
                    neww=1;
                }else {
                    Toast.makeText(NewPassword.this, "Old Password is incorrect", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (neww==1
                        && NewPassword.getText().toString().matches(ConfirmNewPassword.getText().toString())
                        && !NewPassword.getText().toString().matches("")
                        && !ConfirmNewPassword.getText().toString().matches("")){
                    auth.updatePassword(NewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful() || task.isComplete()){
                                Toast.makeText(NewPassword.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                editor.putString("passowrd",NewPassword.getText().toString());
                            }
                        }
                    });
                }
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
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
