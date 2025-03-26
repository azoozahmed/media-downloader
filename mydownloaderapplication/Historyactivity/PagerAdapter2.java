package com.example.mydownloaderapplication.Historyactivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mydownloaderapplication.Historyactivity.ImagesFragment.ImagesFragment;
import com.example.mydownloaderapplication.Historyactivity.VideosFragment.VideosFragment;

public class PagerAdapter2 extends FragmentPagerAdapter {
    int tabcount;
    public PagerAdapter2(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new VideosFragment();
            case 1:
                return new ImagesFragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return tabcount;
    }
}
