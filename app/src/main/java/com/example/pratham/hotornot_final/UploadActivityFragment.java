package com.example.pratham.hotornot_final;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daprlabs.cardstack.SwipeDeck;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import de.hdodenhof.circleimageview.CircleImageView;

public class UploadActivityFragment extends Fragment {

    public static Uri UploadUri;
    public static ImageView clickable,add;
    public static int IMAGE=0;
    public static Button click;
    DatabaseReference reference = FirebaseDatabase.getInstance ().getReference ();
    RelativeLayout YesInternet,internet;
    SharedPreferences preferences ;
    String email,emailName;
    CircleImageView popup;
    public static String nameString,usernameString,score;
    public static ProgressDialog dialog;
    int a,i,numodpost = 1;
    BaseAdapter adapter;
    public UploadActivityFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_activity, container, false);
        preferences = getActivity().getSharedPreferences("loginCredentials", Context.MODE_PRIVATE);
        email = preferences.getString("email",null);
        a = email.indexOf("@");
        emailName = email.substring(0,a);
        SwipeDeck stackView = view.findViewById(R.id.cardViewUpload);
        popup = view.findViewById(R.id.popup);
        internet = view.findViewById(R.id.internet);
        YesInternet = view.findViewById(R.id.YesInternet);
        if(!InternetConnectionCheck.checkConnection(getContext())){
            YesInternet.setVisibility(View.INVISIBLE);
            internet.setVisibility(View.VISIBLE);
        }else{
            YesInternet.setVisibility(View.VISIBLE);
            internet.setVisibility(View.INVISIBLE);
        }
        adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return numodpost;
            }

            @Override
            public Object getItem(int position) {
                return position;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.new_item_card_view, null);
                clickable = view.findViewById(R.id.clickable);
                add = view.findViewById(R.id.add);
                click = view.findViewById(R.id.click);
                click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(getActivity(),
                                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    69);
                        } else {
                            Matisse.from(getActivity())
                                    .choose(MimeType.ofAll())
                                    .maxSelectable(1)
                                    .showSingleMediaType(true)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.9f)
                                    .imageEngine(new PicassoEngine())
                                    .forResult(99);
                        }
                    }
                });
                return view;
            }
        };
        stackView.setAdapter(adapter);
        stackView.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                Toast.makeText(getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void cardSwipedRight(int position) {
                if(IMAGE==1){
                    UploadAsyncTask asyncTask = new UploadAsyncTask(getActivity(), i, emailName, UploadUri,score);
                    asyncTask.execute();
                    Toast.makeText(getContext(), "Uploading", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Add a photo to upload.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void cardsDepleted() {
                numodpost++;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
        getInt();
        dialog = new ProgressDialog (getActivity());
        dialog.setMessage ("Uploading");
        dialog.setTitle ("New Photo");
        dialog.setCancelable (false);
        return view;
    }

    public void getInt(){
        reference.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                DataSnapshot imageValue = dataSnapshot.child ("User").child (emailName).child ("TotalImages");
                DataSnapshot namee= dataSnapshot.child ("User").child (emailName).child ("Name");
                DataSnapshot username = dataSnapshot.child ("User").child (emailName).child ("Username");
                DataSnapshot scoreData = dataSnapshot.child ("User").child (emailName).child ("Score");
                nameString = String.valueOf (namee.getValue ());
                usernameString = String.valueOf (username.getValue ());
                score = String.valueOf(scoreData.getValue());
                if(imageValue.exists ())
                    i = Integer.parseInt (String.valueOf (imageValue.getValue ()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText (getActivity(), "Error 406: " + databaseError.getMessage (), Toast.LENGTH_SHORT).show ();
            }
        });
    }
}
