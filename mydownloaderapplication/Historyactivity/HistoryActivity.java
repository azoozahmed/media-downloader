package com.example.mydownloaderapplication.Historyactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;

import com.example.mydownloaderapplication.R;

import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {
    Button videosBtn;
    Button imagesBtn;

    ViewPager viewPager;
    androidx.viewpager.widget.PagerAdapter pagerAdapter;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        videosBtn = findViewById(R.id.videosBtn);
        imagesBtn = findViewById(R.id.imagesBtn);
        viewPager = findViewById(R.id.historyPager);

        pagerAdapter = new PagerAdapter2(getSupportFragmentManager(),2);
        viewPager.setAdapter(pagerAdapter);


        //********* Action bar**************

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF3700B3")));
        //***************************************

        videosBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(0);
        });
        imagesBtn.setOnClickListener(v -> {
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(1);
        });
        int purple_200=getResources().getColor(R.color.purple_200);
        int purple_500=getResources().getColor(R.color.purple_502);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                videosBtn.setBackgroundColor(purple_200);
                imagesBtn.setBackgroundColor(purple_200);

                if(position == 0){
                    videosBtn.setBackgroundColor( purple_500);
                } else if (position == 1) {
                    imagesBtn.setBackgroundColor( purple_500);
                }
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}