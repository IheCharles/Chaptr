package com.dev.textnet.Userpro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/11/14.
 */

public class SectionPageAdapter3 extends FragmentPagerAdapter {
    private final List<Fragment> fragmentslist= new ArrayList<>();
    private final List<String> stringslist= new ArrayList<>();

    public  void addFragment(Fragment fragment, String title){
        fragmentslist.add(fragment);
        stringslist.add(title);
    }
    public SectionPageAdapter3(FragmentManager fm) {
        super(fm);
    }


    @Override
    public CharSequence getPageTitle(int position) {
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

