package com.alex.yastocks.ui.stock;

import android.util.Log;

import com.alex.yastocks.models.Stock;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class InfoModel {

    private final InfoViewModel viewModel;
    private final Realm realm;

    InfoModel(InfoViewModel viewModel){
        this.viewModel = viewModel;
        realm = Realm.getDefaultInstance();
    }

    public void changeSelectionStatus(String ticker){

        Stock result = realm.where(Stock.class)
                .equalTo("ticker", ticker)
                .findFirst();

        if (result != null) {
            realm.executeTransactionAsync(t -> {

                Stock stock = t.where(Stock.class)
                        .equalTo("ticker", ticker)
                        .findFirst();
                boolean isSelected = stock.isSelected();
                stock.setSelected(!isSelected);

            });
            viewModel.selectionChanged( ! result.isSelected());
        }

    }

}
