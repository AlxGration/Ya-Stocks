package com.alex.yastocks.ui.stock;

import android.os.Bundle;

import com.alex.yastocks.R;
import com.google.android.material.tabs.TabLayout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import io.realm.Realm;


public class InfoActivity extends AppCompatActivity {

    InfoViewModel viewModel;
    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;
    TextView tvError, tvTicker, tvCompanyName;
    ImageButton imgSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Realm.init(InfoActivity.this);

        viewModel = new ViewModelProvider(this).get(InfoViewModel.class);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tvError = findViewById(R.id.tv_error);
        viewModel.getErrorMessageMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvError.setVisibility(View.VISIBLE);
                tvError.setText(s);
                //TODO: call hideError
            }
        });

        tvTicker = findViewById(R.id.tv_ticker);
        tvCompanyName = findViewById(R.id.tv_company_name);
        imgSelector = findViewById(R.id.img_selector);
        viewModel.getIsSelectedMutableLiveData().observe(this, this::setSelectionStatus);

        String ticker = getIntent().getStringExtra("ticker");
        String companyName = getIntent().getStringExtra("companyName");
        boolean isSelected = getIntent().getBooleanExtra("isSelected", false);
        viewModel.rememberTicker(ticker);
        setSelectionStatus(isSelected);
        tvTicker.setText(ticker);
        tvCompanyName.setText(companyName);

    }

    public void onClickSelectorBtn(View view) {
        viewModel.selectorBtnClicked();
    }
    public void onClickBackBtn(View view) {
        onBackPressed();
    }

    public void hideError(){ tvError.setVisibility(View.GONE); }
    public void setSelectionStatus(boolean isSelected){
        imgSelector.setImageResource(
                isSelected?
                        R.drawable.ic_star_selected:
                        R.drawable.ic_star_select
        );
    }
}