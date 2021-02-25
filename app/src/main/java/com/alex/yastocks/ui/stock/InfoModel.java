package com.alex.yastocks.ui.stock;

import android.util.Log;

import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.Stock;

public class InfoModel {

    private final InfoViewModel viewModel;
    private final DBManager db;

    InfoModel(InfoViewModel viewModel){
        this.viewModel = viewModel;
        db = new RealmManager();
    }

    public void changeSelectionStatus(String ticker){

        viewModel.selectionChanged(
                db.changeSelectionStatus(ticker)
        );
    }
}
