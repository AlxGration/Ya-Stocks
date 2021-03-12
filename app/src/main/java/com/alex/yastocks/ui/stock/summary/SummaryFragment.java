package com.alex.yastocks.ui.stock.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.alex.yastocks.R;
import com.alex.yastocks.ui.list.MainActivity;
import com.alex.yastocks.ui.stock.InfoActivity;


public class SummaryFragment extends Fragment {

    private SummaryViewModel viewModel;
    private final String ticker;

    public SummaryFragment(String ticker){
        this.ticker = ticker;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SummaryViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_summary, container, false);

        TextView tvCompanySummary = root.findViewById(R.id.tv_about_company);
        TextView tvCountry = root.findViewById(R.id.tv_county);
        TextView tvSector = root.findViewById(R.id.tv_sector);
        ProgressBar pBar = root.findViewById(R.id.progress_bar);

        viewModel.getCompanySummaryLiveData().observe(this, tvCompanySummary::setText);
        viewModel.getCountryLiveData().observe(this, tvCountry::setText);
        viewModel.getSectorLiveData().observe(this, tvSector::setText);

        viewModel.requestStockSummary(ticker);
        viewModel.getIsLoadingMutableLiveData().observe(this, isLoading ->{
            if (isLoading)
                pBar.setVisibility(View.VISIBLE);
            else
                pBar.setVisibility(View.GONE);
        });

        viewModel.getErrorMessageMutableLiveData().observe(getViewLifecycleOwner(), msg -> {
            ((InfoActivity) getActivity()).showError(msg);
        });
        return root;
    }
}