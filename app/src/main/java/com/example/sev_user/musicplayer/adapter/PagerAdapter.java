package com.example.sev_user.musicplayer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by sev_user on 7/14/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private static final int NUM_ITEMS = 3;
    private ArrayList<Fragment> arrayList;

    public PagerAdapter(FragmentManager fm, ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: {
                return "NGHỆ SỸ";
            }
            case 1: {
                return "ALBUM";
            }
            case 2: {
                return "BÀI HÁT";
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
