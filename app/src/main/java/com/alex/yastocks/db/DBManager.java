package com.alex.yastocks.db;

import com.alex.yastocks.models.Stock;

import java.util.ArrayList;


public abstract class DBManager {

    public abstract ArrayList<Stock> getSelectedStocks();
    public abstract ArrayList<Stock> getFirst24Stocks();
    public abstract ArrayList<Stock> searchStocks(String name);

    public abstract void writeStock(Stock stock);
    public abstract Stock getStock(String ticker);
    public abstract boolean changeSelectionStatus(String ticker);
    public abstract boolean getSelectionStatus(String ticker);

}
