package com.alex.yastocks.ui.main.favourites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.yastocks.R;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StocksListRecyclerAdapter;
import com.alex.yastocks.ui.main.MainActivity;
import com.alex.yastocks.ui.main.stocks.StocksFragment;
import com.alex.yastocks.ui.stock.InfoActivity;

import java.util.ArrayList;


public class FavouriteFragment extends Fragment{

    private FavouriteViewModel viewModel;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getFavouriteStocksFromDB();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stocks_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_stocks_list);
        viewModel.getFavouriteStocksMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Stock>>() {
            @Override
            public void onChanged(ArrayList<Stock> stocks) {
                StocksListRecyclerAdapter adapter = new StocksListRecyclerAdapter(stocks);
                adapter.setOnItemClickListener(((MainActivity)getActivity()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}