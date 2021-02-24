package com.alex.yastocks.ui.list.stocks;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.alex.yastocks.models.Stock;
import java.util.ArrayList;

public class StocksViewModel extends ViewModel {

    private final StocksModel model;

    private final MutableLiveData<ArrayList<Stock>> stocksLiveData;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isLoading;

    public StocksViewModel() {
        model = new StocksModel(this);
        stocksLiveData = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Stock>> getStocksMutableLiveData() { return stocksLiveData; }
    public MutableLiveData<String> getErrorMessageMutableLiveData() { return errorMessage; }
    public MutableLiveData<Boolean> getIsLoadingMutableLiveData() { return isLoading; }

    public void getMostActiveStocksFromDB(){
        model.showStocksFromDB();
    }

    void responseError(String msg){
        errorMessage.setValue(msg);
    }
    void responseMostWatchedSuccess(ArrayList<Stock> stocksList){
        stocksLiveData.setValue(stocksList);
        hideProgressBarLoading();
    }

    void startRequesting(){
        model.startTimerRequestMostActiveStocks();
    }
    void stopRequesting(){
        model.stopTimerRequestMostActiveStocks();
    }

    void showProgressBarLoading(){
        isLoading.setValue(true);
    }
    void hideProgressBarLoading(){
        isLoading.setValue(false);
    }
}
