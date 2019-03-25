package com.example.bookcase;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List FragmentList = new ArrayList();
    private final List FragmentListTitle = new ArrayList();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return (Fragment) FragmentList.get(i);
    }

    public void addFragment(Fragment fragment, String fragTitle){
        FragmentList.add(fragment);
        FragmentListTitle.add(fragTitle);
    }

    @Override
    public int getCount() {
        return FragmentList.size();
    }
}
