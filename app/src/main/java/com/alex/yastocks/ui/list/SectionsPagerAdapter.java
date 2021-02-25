package com.alex.yastocks.ui.list;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alex.yastocks.ui.list.favourites.FavouriteFragment;
import com.alex.yastocks.ui.list.stocks.StocksFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        switch (position){
            case 0:
                return new StocksFragment();
            case 1:

            default:
                return new FavouriteFragment();
        }
    }


    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}