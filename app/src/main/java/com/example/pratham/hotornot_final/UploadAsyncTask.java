package com.example.pratham.hotornot_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

import static com.example.pratham.hotornot_final.UploadActivityFragment.IMAGE;
import static com.example.pratham.hotornot_final.UploadActivityFragment.nameString;
import static com.example.pratham.hotornot_final.UploadActivityFragment.usernameString;

public class UploadAsyncTask extends AsyncTask<Void,Void,Void> {

    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
    private StorageReference storage = FirebaseStorage.getInstance ().getReference ();
    int i;
    private Uri UploadUri;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String randomString,bxs,emailName,score;

    public UploadAsyncTask(Context context,int i,String emailName,Uri UploadUri,String Score) {
        this.context = context;
        this.i = i;
        this.emailName = emailName;
        this.UploadUri = UploadUri;
        randomString = UUID.randomUUID ().toString ();
        score = Score;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        storage.child (emailName).child (UUID.randomUUID ().toString ()).putFile (UploadUri).addOnSuccessListener (new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                i++;
                DatabaseReference emailRef = reference.child ("User").child (emailName).child ("Posts").child (String.valueOf (i));
                DatabaseReference totalimageRef = reference.child ("User").child (emailName).child ("TotalImages");
                emailRef.setValue (randomString);
                totalimageRef.setValue (String.valueOf (i));
                UploadingPosts (taskSnapshot.getDownloadUrl ().toString ());
                Toast.makeText(context, "Post Uploaded Successfully", Toast.LENGTH_SHORT).show();
                IMAGE=0;
                cancel(true);
            }
        }).addOnFailureListener (new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText (context, "Error 405: " + e.getMessage (), Toast.LENGTH_SHORT).show ();
            }
        });
        return null;
    }

    private void UploadingPosts(String string){
        SharedPreferences preferences = context.getSharedPreferences("loginCredentials", Context.MODE_PRIVATE);
        String username = preferences.getString ("email",null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0,a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                IncreaseScore();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Error: " + databaseError.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
        Calendar calendar = Calendar.getInstance ();
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat ("HH:mm:ss", Locale.getDefault ());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("dd/MM/yyyy", Locale.getDefault ());
        DatabaseReference Url= reference.child ("Posts").child (randomString).child ("Url");
        DatabaseReference Name = reference.child ("Posts").child (randomString).child ("Name");
        DatabaseReference Username = reference.child ("Posts").child (randomString).child ("Username");
        DatabaseReference Date= reference.child ("Posts").child (randomString).child ("Date");
        DatabaseReference Time= reference.child ("Posts").child (randomString).child ("Time");
        DatabaseReference Likes= reference.child ("Posts").child (randomString).child ("Likes");
        DatabaseReference DisLikes= reference.child ("Posts").child (randomString).child ("DisLikes");
        Name.setValue (nameString);
        Username.setValue (usernameString);
        Url.setValue (string);
        Likes.setValue ("0");
        DisLikes.setValue ("0");
        Time.setValue (simpleTimeFormat.format (calendar.getTime ()));
        Date.setValue (simpleDateFormat.format (calendar.getTime ()));
    }

    private void IncreaseScore(){
        double scoreInt = Double.parseDouble(score);
        scoreInt = scoreInt + 1.0;
        DatabaseReference scoreRef = reference.child("User").child(bxs).child("Score");
        scoreRef.setValue(String.valueOf(scoreInt));
    }
}
