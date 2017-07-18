package com.example.gabbygiordano.marketplace.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by tanvigupta on 7/12/17.
 */

public class ItemsPagerAdapter extends FragmentPagerAdapter {

    private String[] tabTitles = new String[] {"All", "Books", "Electronics", "Clothes", "Misc"};
    Context context;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public ItemsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    // return the total number of fragments
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    // return fragment to use depending on position
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AllTimelineFragment();
        } else if (position == 1) {
            return new BooksTimelineFragment();
        } else if (position == 2) {
            return new ElectronicsTimelineFragment();
        } else if (position == 3) {
            return new ClothesTimelineFragment();
        } else if (position == 4) {
            return new MiscTimelineFragment();
        } else {
            return null;
        }
    }

    // return page title
    @Override
    public CharSequence getPageTitle(int position) {
        // generate title based on position
        return tabTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public Fragment getRegisteredFragment(String title) {
        int position = -1;
        for (int i = 1; i < tabTitles.length; i++) {
            if (tabTitles[i] == title){
                position = i;
                break;
            }
        }
        return registeredFragments.get(position);
    }
}
