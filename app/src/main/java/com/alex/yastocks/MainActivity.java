package com.alex.yastocks;

import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.yastocks.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TextView tvStocks, tvFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);


        tvStocks = findViewById(R.id.tv_tab_1);
        tvFavourites = findViewById(R.id.tv_tab_2);

        tvStocks.setOnClickListener(view -> viewPager.setCurrentItem(0));
        tvFavourites.setOnClickListener(view -> viewPager.setCurrentItem(1));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    setActive(tvStocks);
                    setInactive(tvFavourites);
                }else{
                    setActive(tvFavourites);
                    setInactive(tvStocks);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {            }
        });

    }

    private void setActive(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.active_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setTextColor(getResources().getColor(R.color.black));
    }
    private void setInactive(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.inactive_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
        tv.setTextColor(getResources().getColor(R.color.grey));
    }
}