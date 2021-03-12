package com.alex.yastocks.ui.stock;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.alex.yastocks.R;
import com.alex.yastocks.ui.stock.chart.ChartFragment;
import com.alex.yastocks.ui.stock.summary.SummaryFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_chart, R.string.tab_summary};
    private final Context mContext;
    private final String ticker;

    public SectionsPagerAdapter(Context context, String ticker, FragmentManager fm) {
        super(fm);
        mContext = context;
        this.ticker = ticker;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:
                return new ChartFragment(ticker);
            case 1:
            default:
                return new SummaryFragment(ticker);
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }
}