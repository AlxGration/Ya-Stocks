package com.alex.yastocks.ui.stock.summary;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class SummaryViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> companySummary;
    private final MutableLiveData<String> country;
    private final MutableLiveData<String> sector;

    private final SummaryModel model;

    public SummaryViewModel(){
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        companySummary = new MutableLiveData<>();
        country = new MutableLiveData<>();
        sector = new MutableLiveData<>();
        model = new SummaryModel(this);
    }

    public MutableLiveData<Boolean> getIsLoadingMutableLiveData() { return isLoading; }
    public MutableLiveData<String> getErrorMessageMutableLiveData() { return errorMessage; }
    public MutableLiveData<String> getCompanySummaryLiveData() { return companySummary; }
    public MutableLiveData<String> getCountryLiveData() { return country; }
    public MutableLiveData<String> getSectorLiveData() { return sector; }

    public void setCompanySummary(String companySummary){
        this.companySummary.setValue(companySummary);
    }
    public void setSector(String sector){
        this.sector.setValue(sector);
    }
    public void setCountry(String country){
        this.country.setValue(country);
    }

    public void requestStockSummary(String ticker){
        isLoading.setValue(true);
        model.requestSummary(ticker);
    }

    void responseError(String msg){errorMessage.setValue(msg);}
    void responseSuccess(){
        isLoading.setValue(false);
    }

}