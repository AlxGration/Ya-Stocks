package com.alex.yastocks.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alex.yastocks.R;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StocksListRecyclerAdapter;

import java.util.ArrayList;

public class SearchListFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_list_search, container, false);

        recyclerView = view.findViewById(R.id.rv_stocks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new StocksListRecyclerAdapter(new ArrayList<>()));

        return view;
    }

    public void setData(ArrayList<Stock> stocksList){
        StocksListRecyclerAdapter adapter = new StocksListRecyclerAdapter(stocksList);
        StocksListRecyclerAdapter.setOnItemClickListener(((SearchActivity)getActivity()));

        recyclerView.setAdapter(adapter);
    }

}