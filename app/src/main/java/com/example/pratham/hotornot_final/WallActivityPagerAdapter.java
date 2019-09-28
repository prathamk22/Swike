package com.example.pratham.hotornot_final;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WallActivityPagerAdapter extends FragmentStatePagerAdapter {
    int num;
    Context context;
    public WallActivityPagerAdapter(FragmentManager fm, int num, Context context) {
        super(fm);
        this.num = num;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new WallActivityProfileView();
            case 1: return new WallActivitySwipe();
            case 2: return new UploadActivityFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
