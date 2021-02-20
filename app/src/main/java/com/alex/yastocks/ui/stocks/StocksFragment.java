package com.alex.yastocks.ui.stocks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.yastocks.R;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StocksListRecyclerAdapter;

import java.util.ArrayList;

public class StocksFragment extends Fragment {

    StocksViewModel viewModel;
    StocksListRecyclerAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(StocksViewModel.class);
    }

    @Override
    public void onPause() {
        super.onPause();
        //viewModel.detach();
    }

    @Override
    public void onResume() {
        super.onResume();
        //viewModel.attach(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stocks_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_stocks_list);
        viewModel.getStocksMutableLiveData().observe(this, new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                adapter = new StocksListRecyclerAdapter(stocks);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        TextView tvError = view.findViewById(R.id.tv_error);
        viewModel.getErrorMessageMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                tvError.setText(msg);
                tvError.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
