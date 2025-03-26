package com.example.mydownloaderapplication.Historyactivity.ImagesFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mydownloaderapplication.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.io.File;

public class ImageViewActivity extends AppCompatActivity {

    String path;
    ImageView imageView;
    ImageButton share;
    ImageButton delete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imageView = findViewById(R.id.imageView);

        share = findViewById(R.id.shareVideo);
        delete = findViewById(R.id.deleteVideo);

        //********* Action bar**************

        getSupportActionBar().hide();
        //***************************************
        Intent intent = getIntent();
        if (intent != null) {
            path = intent.getStringExtra("image");
            imageView.setImageBitmap(BitmapFactory.decodeFile(path));
        }
        share.setOnClickListener(v -> new ShareCompat.IntentBuilder(ImageViewActivity.this).setType("images/*").setStream(Uri.parse(path)).setChooserTitle("Share images").startChooser());
        delete.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(ImageViewActivity.this);
            alertDialogBuilder.setMessage(R.string.DeleteConfirm);
            alertDialogBuilder.setPositiveButton(R.string.Ok, (dialog, which) -> {
                String[] projection = new String[] {MediaStore.Images.Media._ID};
                String selection = MediaStore.Images.Media.DATA +" = ?";
                String[] selectionArgs = new String[]{new File(path).getAbsolutePath()};
                Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(queryUri, projection,selection,selectionArgs,null);
                if(cursor.moveToFirst()){
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                    Uri deletUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    try {
                        contentResolver.delete(deletUri, null, null);
                        Toast.makeText(ImageViewActivity.this, R.string.DeletedSuccessfully, Toast.LENGTH_SHORT).show();
                        finish();
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(ImageViewActivity.this, R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ImageViewActivity.this, R.string.ErrorOccurred, Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            });
            alertDialogBuilder.setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();

        });
    }
}