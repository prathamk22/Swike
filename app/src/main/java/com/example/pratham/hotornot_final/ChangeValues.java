package com.example.pratham.hotornot_final;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class ChangeValues extends AppCompatActivity {

    private String bxs;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    EditText editText;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_values);
        String value = getIntent().getStringExtra("value");
        final String hint = getIntent().getStringExtra("hint");
        TextView view = findViewById(R.id.value);
        editText = findViewById(R.id.editTxt);
        cardView = findViewById(R.id.cardView);
        TextView cancel = findViewById(R.id.cancel);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        view.setTypeface(typeface);
        cancel.setTypeface(typeface);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_down2,R.anim.login_animation);
            }
        });
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString("email", null);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_0);
        cardView.startAnimation(animation);
        int a;
        switch (hint){
            case "Name":editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                break;
            case "Website":editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                break;
            case "Mobile":editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0, a);
        }
        Button button = findViewById(R.id.saveBtn);
        editText.setHint(hint);
        view.setText(value);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (hint){
                    case "Name":editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                        changeName();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                    case "Username":changeUsername();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                    case "Website":editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
                        changeWebsite();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                    case "Bio":changeBio();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                    case "Gender":changeGender();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                    case "Mobile":editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        changeMobile();
                        finish();
                        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
                        break;
                }
            }
        });
    }

    public void changeName(){
        DatabaseReference name = reference.child("User").child(bxs).child("Name");
        if (!editText.getText().toString().matches("")){
            name.setValue(editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
        }
    }
    public void changeUsername(){
        DatabaseReference username = reference.child("User").child(bxs).child("Username");
        if (!editText.getText().toString().matches("")){
            username.setValue(editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter Correct Username", Toast.LENGTH_SHORT).show();
        }
    }
    public void changeWebsite(){
        DatabaseReference website = reference.child("User").child(bxs).child("Website");
        if (!editText.getText().toString().matches("")){
            website.setValue("http://"+editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter Correct Website", Toast.LENGTH_SHORT).show();
        }
    }
    public void changeBio() {
        DatabaseReference Bio = reference.child("User").child(bxs).child("Bio");
        if (!editText.getText().toString().matches("")){
            Bio.setValue(editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter your Bio", Toast.LENGTH_SHORT).show();
        }
    }
    public void changeGender(){
        DatabaseReference Gender = reference.child("User").child(bxs).child("Gender");
        if (!editText.getText().toString().matches("")
                && editText.getText().toString().matches("male" )
                || editText.getText().toString().matches("Male")
                || editText.getText().toString().matches("Female")
                || editText.getText().toString().matches("female")){
            Gender.setValue(editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter Correct Gender", Toast.LENGTH_SHORT).show();
        }
    }
    public void changeMobile(){
        DatabaseReference username = reference.child("User").child(bxs).child("Mobile");
        if (!editText.getText().toString().matches("")){
            username.setValue(editText.getText().toString());
        }else {
            Toast.makeText(this, "Enter Correct Number", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_down,R.anim.login_animation);
    }
}
