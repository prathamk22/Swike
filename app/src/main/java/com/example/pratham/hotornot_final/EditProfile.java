package com.example.pratham.hotornot_final;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.developers.imagezipper.ImageZipper;
import com.fxn.pix.Pix;
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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
    private String bxs;
    UserInfo userInfo;
    CircleImageView editImage,color;
    CustomListView listView;
    ImageView back;
    android.support.v7.widget.Toolbar toolbar;
    EditProfileAdapter adapter;
    private String email,emailName;
    private Uri UploadUri;
    StorageReference storage = FirebaseStorage.getInstance().getReference();
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = findViewById(R.id.actionBar);
        setSupportActionBar(toolbar);
        userInfo = new UserInfo();
        editImage = findViewById(R.id.editImage);
        back = findViewById(R.id.back);
        color = findViewById(R.id.color);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.login_animation,R.anim.login_animation);
            }
        });
        listView = findViewById(R.id.listView);
        adapter = new EditProfileAdapter(userInfo, getApplicationContext());
        if (!InternetConnectionCheck.checkConnection(getApplicationContext())){
            Toast.makeText(this, "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
        }
        getUserData();
        getProfilePic();
        listView.setAdapter(adapter);
        editImage.bringToFront();
        color.bringToFront();

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewProfilePhoto();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent = new Intent(EditProfile.this, ChangeValues.class);
                switch (i){
                    case 0:intent.putExtra("value", "Change Name");
                        intent.putExtra("hint", "Name");
                        startActivity(intent);
                        Log.e("This is this","This is this");
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
                    case 1:intent.putExtra("value", "Change Username");
                        intent.putExtra("hint", "Username");
                        startActivity(intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
                    case 3:intent.putExtra("value", "Change Website");
                        intent.putExtra("hint", "Website");
                        startActivity(intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
                    case 4:intent.putExtra("value", "Change Bio");
                        intent.putExtra("hint", "Bio");
                        startActivity(intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
                    case 5:intent.putExtra("value", "Change Mobile Number");
                        intent.putExtra("hint", "Mobile");
                        startActivity(intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
                    case 6:intent.putExtra("value", "Change Gender");
                        intent.putExtra("hint", "Gender");
                        startActivity(intent);
                        overridePendingTransition(R.anim.login_animation,R.anim.slide_down2);
                        break;
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

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void onBackPressed(){
        super.onBackPressed();
    }

    public void getUserData(){
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        final String username = preferences.getString ("email",null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0,a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot Name = dataSnapshot.child("User").child(bxs).child("Name");
                DataSnapshot Username = dataSnapshot.child("User").child(bxs).child("Username");
                DataSnapshot ProfileUrl = dataSnapshot.child("User").child(bxs).child("ProfilePhoto");
                DataSnapshot TotalImages = dataSnapshot.child("User").child(bxs).child("TotalImages");
                DataSnapshot Mobile = dataSnapshot.child("User").child(bxs).child("Mobile");
                DataSnapshot Website = dataSnapshot.child("User").child(bxs).child("Website");
                DataSnapshot Gender = dataSnapshot.child("User").child(bxs).child("Gender");
                DataSnapshot Bio = dataSnapshot.child("User").child(bxs).child("Bio");
                userInfo.setName(String.valueOf(Name.getValue()));
                userInfo.setProfileUrl(String.valueOf(ProfileUrl.getValue()));
                userInfo.setTotalImages(String.valueOf(TotalImages.getValue()));
                userInfo.setUsername(String.valueOf(Username.getValue()));
                userInfo.setGender(String.valueOf(Gender.getValue()));
                userInfo.setWebsite(String.valueOf(Website.getValue()));
                userInfo.setBio(String.valueOf(Bio.getValue()));
                userInfo.setMobile(String.valueOf(Mobile.getValue()));
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile.this, "Error fetching Data from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getProfilePic() {
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        String username = preferences.getString("email", null);
        int a;
        if (username != null) {
            a = username.indexOf("@");
            bxs = username.substring(0, a);
        }
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = dataSnapshot.child("User").child(bxs).child("ProfilePhoto");
                Glide.with(getApplicationContext()).load(String.valueOf(snapshot.getValue())).into(editImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void NewProfilePhoto() {
        if (ContextCompat.checkSelfPermission(EditProfile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditProfile.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Matisse.from(EditProfile.this)
                    .choose(MimeType.ofAll())
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.9f)
                    .imageEngine(new PicassoEngine())
                    .forResult(101);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Matisse.from(EditProfile.this)
                            .choose(MimeType.ofAll())
                            .maxSelectable(1)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.9f)
                            .imageEngine(new PicassoEngine())
                            .forResult(101);
                } else {
                    Toast.makeText(this, "Permission Not Granted, Cannot Upload Photo right now.", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101) {
            List<String> mSelected = Matisse.obtainPathResult(data);
            Intent intent = new Intent(getApplicationContext(), CropActivity.class);
            Uri newUri = Uri.fromFile(new File(mSelected.get(0)));
            intent.putExtra("imageUri", newUri.toString());
            startActivityForResult(intent,102);
        }
        if (resultCode == 100 && requestCode == 102 && data != null) {
            Uri myUri = Uri.parse(Objects.requireNonNull(data.getExtras()).getString("UriString"));
            File file = new File(myUri.getPath());
            File Compressed = null;
            try {
                Compressed = new ImageZipper(EditProfile.this).setQuality(75).setMaxWidth(640).setMaxHeight(480).setCompressFormat(Bitmap.CompressFormat.PNG).compressToFile(file);
            } catch (IOException e) {
                Toast.makeText(this, "Error 407: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Uri newUri = Uri.fromFile(Compressed);
            UploadUri = newUri;
            editImage.setImageURI(UploadUri);
            UploadProfilePic();
        }
    }

    public void UploadProfilePic() {
        final ProgressDialog dialog = new ProgressDialog(EditProfile.this);
        dialog.setMessage("Updating Profile Photo");
        dialog.show();
        SharedPreferences preferences = getSharedPreferences("loginCredentials", MODE_PRIVATE);
        email = preferences.getString("email", null);
        int a = 0;
        if (email != null) {
            a = email.indexOf("@");
        }
        if (email != null) {
            emailName = email.substring(0, a);
        }
        storage.child(emailName).child(UUID.randomUUID().toString()).putFile(UploadUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                dialog.dismiss();
                DatabaseReference ref = reference.child("User").child(emailName).child("ProfilePhoto");
                ref.setValue(String.valueOf(taskSnapshot.getDownloadUrl()));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditProfile.this, "Error 405: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
