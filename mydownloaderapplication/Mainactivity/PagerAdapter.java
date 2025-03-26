package com.example.mydownloaderapplication.Mainactivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mydownloaderapplication.Mainactivity.Fragments.FacebookFragment.FacebookFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.InstagramFragment.InstagramFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TiktokFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TwitchFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.TwitterFragment;
import com.example.mydownloaderapplication.Mainactivity.Fragments.YoutubeFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    int tabcount;
    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
       switch (position){
           case 0:
               return new InstagramFragment();
           case 1:
               return new YoutubeFragment();
           case 2:
               return new TiktokFragment();
           case 3:
               return new FacebookFragment();
           case 4:
               return new TwitterFragment();
           case 5:
               return new TwitchFragment();
           default:
               return null;
       }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
