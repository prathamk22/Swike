package com.example.pratham.hotornot_final;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth auth;
    MaterialEditText username,password;
    ImageView back;
    Toolbar toolbar;
    TextView toolbarText;
    int LoggedIN=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        toolbarText = findViewById(R.id.toolbarText);
        back = findViewById(R.id.back);
        username = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())) {
            Toast.makeText(this, "No Internet Connection Found. Try again later", Toast.LENGTH_SHORT).show();
        }
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        final String emailString = username.getText().toString();
        final String passwordString = password.getText().toString();
        final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
        dialog.setMessage("Verifying Credential");
        dialog.setCancelable(false);
        dialog.setTitle("Login");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
        toolbarText.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Button login = findViewById(R.id.Login);
        final CountDownTimer count = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(LoggedIN==0){
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Server Error.Please try later.", Toast.LENGTH_SHORT).show();
                }

            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!username.getText().toString().matches("")&& !password.getText().toString().matches("")){
                    count.start();
                        dialog.show();
                        auth.signInWithEmailAndPassword(username.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                editor.putString("email", username.getText().toString());
                                editor.putString("passowrd",password.getText().toString());
                                editor.apply();
                                editor.commit();
                                dialog.dismiss();
                                LoggedIN=1;
                                Intent intent = new Intent(LoginActivity.this, WallActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra ("directly","yes");
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Logged in Successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error 402: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }else{
                    Toast.makeText(LoginActivity.this, "Enter all fields", Toast.LENGTH_SHORT).show();
                    if(emailString.matches("")){
                        username.setError("*");
                    }
                    if(passwordString.matches("")){
                        password.setError("*");
                    }
                }
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
}
