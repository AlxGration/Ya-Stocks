package com.alex.yastocks.ui.main.stocks;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.yastocks.ui.main.MainActivity;
import com.alex.yastocks.R;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StocksListRecyclerAdapter;

import java.util.ArrayList;

public class StocksFragment extends Fragment {

    StocksViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StocksViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getMostActiveStocksFromDB();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.startRequesting();// api request MostActiveStocks
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stopRequesting();// api request MostActiveStocks
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_stocks_list);
        viewModel.getStocksMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                StocksListRecyclerAdapter adapter = new StocksListRecyclerAdapter(stocks);
                adapter.setOnItemClickListener(((MainActivity)getActivity()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });


        viewModel.getErrorMessageMutableLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                ((MainActivity)getActivity()).showError(msg);
            }
        });

        ProgressBar pBar = view.findViewById(R.id.progressBar);
        viewModel.getIsLoadingMutableLiveData().observe(getViewLifecycleOwner(), isLoading->{
            if (isLoading)
                pBar.setVisibility(View.VISIBLE);
            else
                pBar.setVisibility(View.GONE);
        });


        showStocksListFromDb();

        return view;
    }

    public void showStocksListFromDb(){
        viewModel.showProgressBarLoading();
        viewModel.getMostActiveStocksFromDB();
        // прогрес бар прячется в viewModel
        // после загрузки данных из (БД)
        // либо старых, либо после выполненного запроса на сервер
    }
}
