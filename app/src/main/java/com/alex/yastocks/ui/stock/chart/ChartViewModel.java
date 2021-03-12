package com.alex.yastocks.ui.stock.chart;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.yastocks.R;
import com.alex.yastocks.models.ChartInterval;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;


public class ChartViewModel extends ViewModel {

    private final ChartModel model;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<String> price;
    private final MutableLiveData<String> priceChange;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<LineDataSet> chartData;


    public ChartViewModel(){
        model = new ChartModel(this);
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        chartData = new MutableLiveData<>();
        price = new MutableLiveData<>();
        priceChange = new MutableLiveData<>();
    }


    public MutableLiveData<Boolean> getIsLoadingMutableLiveData() { return isLoading; }
    public MutableLiveData<String> getErrorMessageMutableLiveData() { return errorMessage; }
    public MutableLiveData<String> getPriceChangeMutableLiveData() { return priceChange; }
    public MutableLiveData<String> getPriceMutableLiveData() { return price; }
    public MutableLiveData<LineDataSet> getChartDataMutableLiveData() { return chartData; }

    public void requestChart(String ticker, ChartInterval interval){
        isLoading.setValue(true);

        int endTime = (int)( System.currentTimeMillis() / 1000);
        int startTime = 0;
        switch(interval) {
            case DAY://1d
                startTime = endTime - (60 * 60 * 24);
                break;
            case WEEK://1w
                startTime = endTime - (60 * 60 * 24 * 7);
                break;
            case MONTH://1m
                startTime = endTime - (60 * 60 * 24 * 30);
                break;
            case YEAR://1y
                startTime = endTime - (60 * 60 * 24 * 365);
                break;
        }
        model.requestChartData(ticker, interval.getInterval(), startTime);
    }

    public void setChartDataEntries(List<Entry> entries){
        LineDataSet dataSet = new LineDataSet(entries, ""); // add entries to dataset
        dataSet.setColor(R.color.black);
        dataSet.setValueTextColor(R.color.black);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawFilled(true);

        chartData.setValue(dataSet);
    }

    void responseError(String msg){errorMessage.setValue(msg);}
    void responseSuccess(){ isLoading.setValue(false); }
    void setPrice(String price){ this.price.setValue(price);}
    void setPriceChange(String priceChange){ this.priceChange.setValue(priceChange);}
}