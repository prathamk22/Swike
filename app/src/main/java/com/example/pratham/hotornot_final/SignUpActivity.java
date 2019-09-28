package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private MaterialEditText name,email,username,password;
    ArrayList<String> usernameList;
    TextView toolbarText;
    Button signUp;
    ImageView back;
    int match=0;
    Toolbar toolbar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.TYPE_STATUS_BAR);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        usernameList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        toolbarText = findViewById(R.id.toolbarText);
        back = findViewById(R.id.back);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);
        signUp = findViewById(R.id.SignUp);
        name.setTypeface(typeface);
        email.setTypeface(typeface);
        username.setTypeface(typeface);
        toolbarText.setTypeface(typeface);
        getUserNameList();
        final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
        dialog.setMessage("Creating Account");
        dialog.setCancelable(false);
        dialog.setTitle("Sign up");
        final SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNull()){
                    if (UsernameValidity()){
                        dialog.show();
                        auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            editor.putString("email", email.getText().toString());
                                            editor.putString("passowrd",password.getText().toString());
                                            editor.apply();
                                            editor.commit();
                                            dialog.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this, WallActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Error 402: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    CreateNewUser();
                                    Toast.makeText(SignUpActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUpActivity.this, "Error Code 401: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        username.setError("*");
                        username.setFocusable(true);
                        Toast.makeText(SignUpActivity.this, "Username not available.Try something different", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(SignUpActivity.this, "Enter all the fields", Toast.LENGTH_SHORT).show();
                    if (name.getText().toString().matches("")) {
                        name.setError("*");
                    }
                    if (email.getText().toString().matches("")) {
                        email.setError("*");
                    }
                    if (username.getText().toString().matches("")) {
                        username.setError("*");
                    }
                    if (password.getText().toString().matches("")) {
                        password.setError("*");
                    }
                }
            }
        });
    }

    public boolean UsernameValidity(){
        if (usernameList.size()!=0){
            for (int i=0; i<usernameList.size(); i++){
                match=0;
                if(username.getText().toString().matches(usernameList.get(i))){
                    match=1;
                }
            }
            if (match==0){
                return true;
            }
        }else{
            return true;
        }
        return false;
    }

    public boolean checkNull(){
        if(!name.getText().toString().matches("")
                && !email.getText().toString().matches("")
                && !username.getText().toString().matches("")
                && !password.getText().toString().matches("")){
            return true;
        }
        return false;
    }

    public void CreateNewUser(){
        int a = email.getText().toString().indexOf("@");
        String b = email.getText().toString().substring(0,a);
        DatabaseReference NAME = databaseReference.child("User").child(b).child("Name");
        DatabaseReference USERNAME = databaseReference.child("User").child(b).child("Username");
        DatabaseReference TotalImages= databaseReference.child("User").child(b).child("TotalImages");
        DatabaseReference Mobile= databaseReference.child("User").child(b).child("Mobile");
        DatabaseReference Website= databaseReference.child("User").child(b).child("Website");
        DatabaseReference Bio= databaseReference.child("User").child(b).child("Bio");
        DatabaseReference Score= databaseReference.child("User").child(b).child("Score");
        DatabaseReference Gender= databaseReference.child("User").child(b).child("Gender");
        Mobile.setValue("");
        Website.setValue("");
        Bio.setValue("");
        Score.setValue("0.0");
        Gender.setValue("Male");
        NAME.setValue(name.getText().toString());
        USERNAME.setValue(username.getText().toString());
        TotalImages.setValue ("0");
    }

    public void getUserNameList(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernameList.clear();
                DataSnapshot user = dataSnapshot.child("User");
                if (user.exists()){
                    for (DataSnapshot userChild  :user.getChildren()){
                        if (userChild.exists())
                            usernameList.add(String.valueOf(userChild.child("Username").getValue()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SignUpActivity.this, "Error Connecting to the server. Please try again later", Toast.LENGTH_SHORT).show();
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
