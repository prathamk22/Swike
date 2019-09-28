package com.example.pratham.hotornot_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    CircleImageView imageView;
    ListView listView;
    Toolbar toolbar;
    TextView logOut,userId;
    String[] a = {"Password",
            "Login Verification"
            ,"Notification Settings"
            ,"Support"
            ,"Privacy"
            ,"Terms of Service"
            ,"Clear Cache"};
    private LayoutInflater inflater;
    Intent intent;
    FirebaseAuth user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        listView = findViewById(R.id.listView);
        toolbar = findViewById(R.id.actionBar);
        logOut = findViewById(R.id.log_out);
        userId = findViewById(R.id.userID);
        imageView = findViewById(R.id.backk);
        user = FirebaseAuth.getInstance();
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())) {
            Toast.makeText(this, "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
        }
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return a.length;
            }

            @Override
            public Object getItem(int position) {
                return a[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int i, View view, ViewGroup parent) {
                if (view==null){
                    view  = inflater.inflate (R.layout.settings_layout, null,false);
                    TextView textView = view.findViewById(R.id.MainText);
                    Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
                    textView.setText(a[i]);
                    textView.setTypeface(typeface);
                   view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (i){
                                case 0:intent = new Intent(getApplicationContext(), NewPassword.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 1:intent = new Intent(getApplicationContext(), LoginVerification.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 2:intent = new Intent(getApplicationContext(), NotificationSettings.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 3:intent = new Intent(getApplicationContext(), Support.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 4:intent = new Intent(getApplicationContext(), Privacy.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 5:intent = new Intent(getApplicationContext(), TermsAndCondition.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                                    break;
                                case 6://intent = new Intent(getApplicationContext(), ClearCache.class);
                                    //startActivity(intent);
                                    break;
                            }
                        }
                    });
                }
                return view;
            }
        });
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        toolbar.setLayoutParams(params);
        logOut.setTypeface(typeface);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString ("email",null);
        userId.setText(username);
        userId.setTypeface(typeface);
    }

    public void LogOut(){
        AlertDialog dialog;
        final AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
        builder.setCancelable(true);
        builder.setTitle("Log out");
        builder.setMessage("Log out of Blow It ");
        final SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user.signOut();
                editor.putString("email", "");
                editor.putString("passowrd","");
                editor.apply();
                editor.commit();
                Toast.makeText(SettingsActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
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
