package com.example.juraj.note.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.juraj.note.fragments.FragmentCalendar;
import com.example.juraj.note.fragments.FragmentMap;
import com.example.juraj.note.fragments.FragmentNotes;

/**
 * Created by Juraj on 18.11.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch(position){
            case 0:
                fragment = FragmentMap.newInstance("", "");
                break;
            case 1:
                fragment = FragmentNotes.newInstance("", "");
                break;
            case 2:
                fragment = FragmentCalendar.newInstance("", "");
                break;
            default:
                fragment = FragmentNotes.newInstance("", "");
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Map";
            case 1:
                return "Notes";
            case 2:
                return "Calendar";
        }
        return null;
    }
}
