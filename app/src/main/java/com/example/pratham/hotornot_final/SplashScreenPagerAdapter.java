package com.example.pratham.hotornot_final;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

class SplashScreenPagerAdapter extends FragmentStatePagerAdapter{
    int num;
    public SplashScreenPagerAdapter(FragmentManager fm,int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new SignUpFragment1();
            case 1: return new SignUpFragment2();
            case 2: return new SignUpFragment3();
            case 3: return new SignUpFragment4();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
