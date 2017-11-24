package com.example.juraj.note.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.juraj.note.fragments.AbstractFragent;
import com.example.juraj.note.fragments.FragmentCalendar;
import com.example.juraj.note.fragments.FragmentMap;
import com.example.juraj.note.fragments.FragmentNotes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juraj on 18.11.2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    List<AbstractFragent> fragments;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<>();
        fragments.add(FragmentMap.newInstance("", ""));
        fragments.add(FragmentNotes.newInstance("", ""));
        fragments.add(FragmentCalendar.newInstance("", ""));

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return fragments.get(position).getTitle();
    }
}
