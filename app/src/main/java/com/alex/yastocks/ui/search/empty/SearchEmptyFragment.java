package com.alex.yastocks.ui.search.empty;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.yastocks.R;
import com.alex.yastocks.models.PrefsManager;
import com.alex.yastocks.ui.search.SearchActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;



public class SearchEmptyFragment extends Fragment implements View.OnClickListener{

  
    private ChipGroup cgPopRow_1, cgPopRow_2, cgTempRow_1, cgTempRow_2;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_empty_search, container, false);
        cgPopRow_1 = view.findViewById(R.id.chip_group_popular_1);
        cgPopRow_2 = view.findViewById(R.id.chip_group_popular_2);

        cgTempRow_1 = view.findViewById(R.id.chip_group_temp_1);
        cgTempRow_2 = view.findViewById(R.id.chip_group_temp_2);

        // возьмем список популярных акций из строковых ресурсов :) и отобразим его в виде Chip'ов
        fillChipGroup(cgPopRow_1, cgPopRow_2, getResources().getStringArray(R.array.popular_stocks));

        PrefsManager prefsManager = new PrefsManager(getContext());
        fillChipGroup(cgTempRow_1, cgTempRow_2, prefsManager.getLastSearches());

        return view;
    }


    private void fillChipGroup(ChipGroup row_1, ChipGroup row_2, String[] array){
        int n = array.length;
        if (n==1 && array[0].isEmpty()) return;
        int half = (n+1)/2;

        // заполнение в 2 ряда
        for(int i = 0; i < n; i ++){
            Chip chip = new Chip(getContext());
            chip.setOnClickListener(SearchEmptyFragment.this);
            chip.setText(array[i]);
            if (i < half){
                row_1.addView(chip);
            }else{
                row_2.addView(chip);
            }
        }
    }

    // отправка текста с нажатого Chip'а в SearchView in SearchActivity
    @Override
    public void onClick(View view) {
        String companyName = ((Chip)view).getText().toString();
        ((SearchActivity)getActivity()).startSearchWith(companyName);
    }
}