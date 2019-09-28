package com.example.pratham.hotornot_final;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.developers.imagezipper.ImageZipper;
import com.fxn.pix.Pix;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.pratham.hotornot_final.UploadActivityFragment.UploadUri;
import static com.example.pratham.hotornot_final.UploadActivityFragment.add;
import static com.example.pratham.hotornot_final.UploadActivityFragment.clickable;
import static com.example.pratham.hotornot_final.UploadActivityFragment.IMAGE;
import static com.example.pratham.hotornot_final.UploadActivityFragment.click;
import static com.example.pratham.hotornot_final.WallActivitySwipe.lottieAnimationView;

public class WallActivity extends AppCompatActivity {

    ViewPager pagerView;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        Log.e("Starts","Again and Again");
        String isDirectly = getIntent ().getStringExtra ("directly");
        if(isDirectly !=null && isDirectly.matches("yes")){
            if(!InternetConnectionCheck.checkConnection(getApplicationContext())){
                Toast.makeText(getApplicationContext(), "No Internet connection Found.Please try again later", Toast.LENGTH_SHORT).show();
            }else{
                AuthAsyncTask authAsyncTask = new AuthAsyncTask (WallActivity.this);
                authAsyncTask.execute ();
            }
        }
        pagerView = findViewById(R.id.pagerView);
        tabLayout = findViewById(R.id.tabLayout);
        WallActivityPagerAdapter activityPagerAdapter = new WallActivityPagerAdapter(getSupportFragmentManager(),3,getApplicationContext());
        pagerView.setAdapter(activityPagerAdapter);
        pagerView.setCurrentItem(1);
        pagerView.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(pagerView, true);
        tabLayout.getTabAt(0).setText("Profile");
        tabLayout.getTabAt(1).setIcon(R.drawable.new_pngnew);
        tabLayout.getTabAt(2).setText("Upload");
        tabLayout.setSelectedTabIndicatorHeight(4);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, getStatusBarHeight(), 0, 0);
        tabLayout.setLayoutParams(params);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerView.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99 && resultCode == RESULT_OK) {
            List<String> mSelected = Matisse.obtainPathResult(data);
            Intent intent = new Intent(getApplicationContext(), CropActivity.class);
            Uri newUri = Uri.fromFile(new File(mSelected.get(0)));
            intent.putExtra("imageUri", newUri.toString());
            startActivityForResult(intent,100);
        }

        if(resultCode==100 && requestCode==100 && data!=null){
            Uri myUri = Uri.parse(data.getExtras().getString("UriString"));
            File file = new File (myUri.getPath ());
            File Compressed=null;
            try {
                Compressed = new ImageZipper(getApplicationContext()).setQuality (75).setMaxWidth (640).setMaxHeight (480).setCompressFormat (Bitmap.CompressFormat.PNG).compressToFile (file);
            } catch (IOException e) {
                Toast.makeText (getApplicationContext(), "Error 407: " + e.getMessage (), Toast.LENGTH_SHORT).show ();
            }
            UploadUri = Uri.fromFile (Compressed);
            clickable.setImageURI(UploadUri);
            add.setVisibility(View.INVISIBLE);
            click.setVisibility(View.INVISIBLE);
            IMAGE=1;
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 69: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Matisse.from(WallActivity.this)
                            .choose(MimeType.ofAll())
                            .maxSelectable(1)
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.9f)
                            .imageEngine(new PicassoEngine())
                            .forResult(99);
                } else {
                    Toast.makeText(this, "Permission Not Granted, Cannot Upload Photo right now.", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
