package com.example.mydownloaderapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.example.mydownloaderapplication.Mainactivity.MainActivity;

import java.util.Locale;
public class StartActivity extends AppCompatActivity {
    TextView name1;
    TextView name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        //**********Check arabic language***************
        if (Locale.getDefault().getLanguage().equals("ar")){
            name1.setText(" تنزيل");
            name2.setText(" الميديا");
        }
        //*******************************************
        new Handler().postDelayed(() -> {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        },2500);
    }
}