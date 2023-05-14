package com.example.textnet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/06/21.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentslist= new ArrayList<>();
    private final List<String> stringslist= new ArrayList<>();

    public  void addFragment(Fragment fragment, String title){
        fragmentslist.add(fragment);
        stringslist.add(title);
    }
    public  SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New";
            case 1:
                return "Subscribed";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentslist.get(position);
    }


    @Override
    public int getCount() {
        return fragmentslist.size();
    }

}

