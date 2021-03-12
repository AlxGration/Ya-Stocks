package com.alex.yastocks.ui.stock;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class InfoViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isSelected;
    private final InfoModel model;
    private String ticker;


    public InfoViewModel(){
        errorMessage = new MutableLiveData<>();
        isSelected = new MutableLiveData<>();
        ticker = "";
        model = new InfoModel(this);
    }

    public MutableLiveData<String> getErrorMessageMutableLiveData() { return errorMessage; }
    public MutableLiveData<Boolean> getIsSelectedMutableLiveData() { return isSelected; }
    public void rememberTicker(String ticker){this.ticker = ticker;}

    public void selectorBtnClicked(){
        model.changeSelectionStatus(ticker);
    }

    public void selectionChanged(boolean selected){
        isSelected.setValue(selected);
    }
}
