package com.alex.yastocks.ui.stock;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.alex.yastocks.R;
import com.alex.yastocks.receivers.NetworkStateChangeReceiver;
import com.alex.yastocks.ui.stock.summary.SummaryFragment;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.realm.Realm;


public class InfoActivity extends AppCompatActivity implements
        NetworkStateChangeReceiver.NetworkStateChangeListener{

    private InfoViewModel viewModel;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TextView tvError;
    private ImageButton imgSelector;
    private NetworkStateChangeReceiver receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        String ticker = getIntent().getStringExtra("ticker");

        viewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, ticker,  getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tvError = findViewById(R.id.tv_error);
        viewModel.getErrorMessageMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
               showError(s);
                //TODO: call hideError somewhere
            }
        });

        TextView tvTicker = findViewById(R.id.tv_ticker);
        TextView tvCompanyName = findViewById(R.id.tv_company_name);
        imgSelector = findViewById(R.id.img_selector);

        viewModel.getIsSelectedMutableLiveData().observe(this, this::setSelectionStatusImage);


        String companyName = getIntent().getStringExtra("companyName");
        boolean isSelected = getIntent().getBooleanExtra("isSelected", false);
        viewModel.rememberTicker(ticker);
        setSelectionStatusImage(isSelected);
        tvTicker.setText(ticker);
        tvCompanyName.setText(companyName);

        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    public void onClickSelectorBtn(View view) {
        viewModel.selectorBtnClicked();
    }
    public void onClickBackBtn(View view) {
        onBackPressed();
    }

    public void hideError(){ tvError.setVisibility(View.GONE); }
    public void showError(String msg){
        tvError.setVisibility(View.VISIBLE);
        tvError.setText(msg);
    }
    public void setSelectionStatusImage(boolean isSelected){
        imgSelector.setImageResource(
                isSelected?
                        R.drawable.ic_star_selected:
                        R.drawable.ic_star_select
        );
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            hideError();
        }else {
            showError("Network Status: false");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) receiver.detach();
    }
}