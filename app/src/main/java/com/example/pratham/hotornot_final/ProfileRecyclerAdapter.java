package com.example.pratham.hotornot_final;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ProfileRecyclerAdapter extends FragmentStatePagerAdapter {

    int num;
    public ProfileRecyclerAdapter(FragmentManager fm,int number) {
        super(fm);
        num = number;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:return new ProfileViewFragment1();
            case 1:return new ProfileViewFragment2();
            case 2:return new ProfileViewFragment3();
            case 3:return new ProfileViewFragment4();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
