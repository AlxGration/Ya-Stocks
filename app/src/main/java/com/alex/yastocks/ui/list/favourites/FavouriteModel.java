package com.alex.yastocks.ui.list.favourites;

import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;

public class FavouriteModel {

    private final DBManager db;
    private final FavouriteViewModel viewModel;

    FavouriteModel(FavouriteViewModel viewModel){
        this.viewModel = viewModel;
        db = new RealmManager();
    }

    public void showFavouriteStocksFromDB(){
        viewModel.responseFavouriteStocksSuccess(
                db.getSelectedStocks()
        );
    }

}
