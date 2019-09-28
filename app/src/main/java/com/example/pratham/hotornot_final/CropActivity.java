package com.example.pratham.hotornot_final;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.steelkiwi.cropiwa.AspectRatio;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;

import java.io.File;
import java.io.IOException;

public class CropActivity extends AppCompatActivity {

    ProgressDialog dialog;
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        final CropIwaView cropIwaView = findViewById(R.id.cropView);
        back = findViewById(R.id.back);
        dialog = new ProgressDialog (CropActivity.this);
        dialog.setMessage ("Cropped Image is loading");
        dialog.setCancelable (false);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/avenir.otf");
        back.setTypeface(typeface);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cropIwaView.configureOverlay()
                .setDynamicCrop(true)
                .setAspectRatio(AspectRatio.IMG_SRC)
                .apply();
        cropIwaView.setErrorListener(new CropIwaView.ErrorListener() {
            @Override
            public void onError(Throwable e) {
                Toast.makeText(CropActivity.this, "Error"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        cropIwaView.setCropSaveCompleteListener(new CropIwaView.CropSaveCompleteListener() {
            @Override
            public void onCroppedRegionSaved(Uri bitmapUri) {
                dialog.dismiss();
                Toast.makeText(CropActivity.this, "Crop Image Successfull", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("UriString", bitmapUri.toString());
                setResult(100, intent);
                finish();
            }
        });
        cropIwaView.configureImage()
                .setMinScale(0.3f)
                .setMaxScale(3f)
                .apply();

        Uri myUri = Uri.parse(getIntent().getExtras().getString("imageUri"));
        cropIwaView.setImageUri(myUri);
        File file = null;
        try {
            file = File.createTempFile("image",".png");
        } catch (IOException e) {
            Toast.makeText(this, "Error here also " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        final Uri uri = Uri.fromFile(file);
        TextView floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setTypeface(typeface);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show ();
                cropIwaView.crop(new CropIwaSaveConfig.Builder(uri).setCompressFormat(Bitmap.CompressFormat.PNG).setQuality(100).build());
            }
        });
    }
}
