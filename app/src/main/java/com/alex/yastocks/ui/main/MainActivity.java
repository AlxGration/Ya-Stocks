package com.alex.yastocks.ui.main;

import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.alex.yastocks.R;
import com.alex.yastocks.receivers.NetworkStateChangeReceiver;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements NetworkStateChangeReceiver.NetworkStateChangeListener{

    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    TextView tvStocks, tvFavourites, tvError;
    NetworkStateChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(MainActivity.this);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        tvStocks = findViewById(R.id.tv_tab_1);
        tvFavourites = findViewById(R.id.tv_tab_2);
        tvError = findViewById(R.id.tv_error);

        tvStocks.setOnClickListener(view -> viewPager.setCurrentItem(0));
        tvFavourites.setOnClickListener(view -> viewPager.setCurrentItem(1));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    setActiveTab(tvStocks);
                    setInactiveTab(tvFavourites);
                }else{
                    setActiveTab(tvFavourites);
                    setInactiveTab(tvStocks);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {            }
        });

        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void showError(String msg){
        tvError.setText(msg);
        tvError.setVisibility(View.VISIBLE);
    }

    public void hideError(){
        tvError.setVisibility(View.GONE);
    }


    // вызывается в случае включения интернета
    /*
    private void updateStocksList(){
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + 0);

        if (fragment instanceof StocksFragment){
            ((StocksFragment)fragment).showStocksListFromDb();
        }else{
            Log.e("TAG", "cant update stocks list, 'cause fragment is "
                    + fragment.toString()+" but expected StocksFragment at position 0");
        }
    }

     */

    private void setActiveTab(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.active_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        tv.setTextColor(getResources().getColor(R.color.black));
    }
    private void setInactiveTab(TextView tv){
        if (tv == null) return;;

        tv.setTextSize(getResources().getInteger(R.integer.inactive_tap_size));
        tv.setTypeface(tv.getTypeface(), Typeface.NORMAL);
        tv.setTextColor(getResources().getColor(R.color.grey));
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        Log.e("TAG", "onNetworkStateChanged "+isConnected);
        if (isConnected) {
            hideError();
        }else {
            showError("Network Status: " + isConnected);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) receiver.detach();
    }
}