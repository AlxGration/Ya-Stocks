package com.alex.yastocks.db;

import com.alex.yastocks.models.Stock;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmManager extends DBManager{

    private final Realm realm = Realm.getDefaultInstance();

    private enum STOCK{
        isSelected,
        companyName,
        ticker,
    }

    @Override
    public ArrayList<Stock> getSelectedStocks() {
        RealmResults<Stock> results = realm.where(Stock.class)
                .equalTo(STOCK.isSelected.name(), true)
                .findAllAsync();
        return new ArrayList<>(results);
    }

    @Override
    public ArrayList<Stock> getFirst24Stocks() {
        RealmResults<Stock> results = realm.where(Stock.class)
                .limit(24)
                .findAllAsync();

        return new ArrayList<>(results);
    }

    @Override
    public ArrayList<Stock> searchStocks(String name) {
        name = name+"*";
        return new ArrayList<>(
                realm.where(Stock.class)
                    .like(STOCK.companyName.name(), name, Case.INSENSITIVE)
                    .or()
                    .like(STOCK.ticker.name(), name, Case.INSENSITIVE)
                    .sort(STOCK.companyName.name(), Sort.ASCENDING)
                .findAllAsync()
        );
    }

    @Override
    public void writeStock(Stock stock) {
        realm.executeTransactionAsync(t->{
            // save stocks selection status, if exist
            Stock result = t.where(Stock.class)
                    .equalTo(STOCK.ticker.name(), stock.getTicker())
                    .findFirst();
            if (result != null) stock.setSelected(result.isSelected());

            t.insertOrUpdate(stock);
        });
    }

    @Override
    public Stock getStock(String ticker) {
        return realm.where(Stock.class)
                .equalTo(STOCK.ticker.name(), ticker)
                .findFirst();
    }

    @Override
    public boolean changeSelectionStatus(String ticker) {
        Stock s = getStock(ticker);
        realm.executeTransactionAsync(t -> {

            Stock stock = t.where(Stock.class)
                    .equalTo(STOCK.ticker.name(), ticker)
                    .findFirst();
            boolean isSelected = stock.isSelected();
            stock.setSelected(!isSelected);
        });
        return !s.isSelected();
    }

    @Override
    public boolean getSelectionStatus(String ticker) {
        return getStock(ticker).isSelected();
    }
}
