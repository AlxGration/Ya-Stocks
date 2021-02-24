package com.alex.yastocks.ui.list.favourites;

import com.alex.yastocks.models.Stock;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavouriteModel {

    private final Realm realm;
    private final FavouriteViewModel viewModel;

    FavouriteModel(FavouriteViewModel viewModel){
        this.viewModel = viewModel;
        realm = Realm.getDefaultInstance();
    }

    public void showFavouriteStocksFromDB(){

        RealmResults<Stock> results = realm.where(Stock.class)
                .equalTo("isSelected", true)
                .findAllAsync();

        ArrayList<Stock> stocksList = new ArrayList<>(results);

        viewModel.responseFavouriteStocksSuccess(stocksList);
    }

}
