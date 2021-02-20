package com.alex.yastocks.ui.stocks;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alex.yastocks.models.Stock;
import java.util.ArrayList;

public class StocksViewModel extends ViewModel {

    private StocksModel model;
    Context context;

    private MutableLiveData<ArrayList<Stock>> stocksLiveData;
    private MutableLiveData<String> errorMessage;

    public StocksViewModel() {
        model = new StocksModel(this);
        stocksLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();

        //TODO: call your Rest API in init method
        init();
    }

    public MutableLiveData<ArrayList<Stock>> getStocksMutableLiveData() { return stocksLiveData; }
    public MutableLiveData<String> getErrorMessageMutableLiveData() { return errorMessage; }

    private  void init(){
        model.mostActiveStocks();
    }

    void requestError(String msg){
        errorMessage.setValue(msg);
    }
    void requestMostWatchedSuccess(ArrayList<Stock> stocksList){
        stocksLiveData.setValue(stocksList);
    }
}
