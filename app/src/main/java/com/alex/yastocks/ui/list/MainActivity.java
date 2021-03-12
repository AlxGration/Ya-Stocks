package com.alex.yastocks.ui.list;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.alex.yastocks.R;
import com.alex.yastocks.models.StocksListRecyclerAdapter;
import com.alex.yastocks.receivers.NetworkStateChangeReceiver;
import com.alex.yastocks.ui.search.SearchActivity;
import com.alex.yastocks.ui.stock.InfoActivity;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements
        NetworkStateChangeReceiver.NetworkStateChangeListener,
        StocksListRecyclerAdapter.IonItemClickListener{

    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TextView tvStocks, tvFavourites, tvError;
    private NetworkStateChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(MainActivity.this);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
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
        if (isConnected) {
            hideError();
        }else {
            showError("Network Status: false" );
        }
    }


    // обработчик нажатия на элемент списка
    // открывает акнивность информации об акции
    @Override
    public void startInfoActivityWith(String ticker, String companyName, boolean isSelected) {
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("isSelected", isSelected);
        intent.putExtra("ticker", ticker);
        intent.putExtra("companyName", companyName);

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) receiver.detach();
    }

    public void onClickSearch(View view) {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}