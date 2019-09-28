package com.example.pratham.hotornot_final;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.content.Context.MODE_PRIVATE;

public class AuthAsyncTask extends AsyncTask<Void,Void,Void> {

    private Context context;
    private FirebaseAuth auth = FirebaseAuth.getInstance ();
    public AuthAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
            SharedPreferences preferences = context.getSharedPreferences("loginCredentials", MODE_PRIVATE);
            String username = preferences.getString ("email",null);
            String password = preferences.getString ("passowrd",null);
            if(username!=null &&password!=null){
                auth.signInWithEmailAndPassword (username,password).addOnFailureListener (new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText (context, "Error 410: " + e.getMessage (), Toast.LENGTH_SHORT).show ();
                        cancel (true);
                        Intent intent = new Intent (context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity (intent);
                    }
                });
            }
        return null;
    }
}