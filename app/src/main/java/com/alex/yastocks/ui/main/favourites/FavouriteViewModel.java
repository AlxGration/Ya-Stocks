package com.alex.yastocks.ui.main.favourites;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.alex.yastocks.models.Stock;

import java.util.ArrayList;

public class FavouriteViewModel extends ViewModel {

    private final FavouriteModel model;
    private final MutableLiveData<ArrayList<Stock>> stocksLiveData;

    public FavouriteViewModel(){
        stocksLiveData = new MutableLiveData<>();
        model = new FavouriteModel(this);
    }

    public MutableLiveData<ArrayList<Stock>> getFavouriteStocksMutableLiveData() { return stocksLiveData; }

    public void getFavouriteStocksFromDB(){
        model.showFavouriteStocksFromDB();
    }

    public void responseFavouriteStocksSuccess(ArrayList<Stock> stocks){
        stocksLiveData.setValue(stocks);
    }
}