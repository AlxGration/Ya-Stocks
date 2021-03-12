package com.alex.yastocks.db;

import com.alex.yastocks.models.ChartPoint;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StockSummary;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public abstract class DBManager {

    public abstract ArrayList<Stock> getSelectedStocks();
    public abstract ArrayList<Stock> getFirst24Stocks();
    public abstract boolean getSelectionStatus(String ticker);
    public abstract StockSummary getStockSummary(String ticker);
    public abstract Stock getStock(String ticker);
    public abstract ArrayList<Stock> searchStocks(String ticker);
    public abstract TreeSet<ChartPoint> getStockChartPoints(String ticker, String interval, int startTime);

    //public abstract StockChart getStockChartData(String ticker, String interval);




    public abstract void writeStock(Stock stock);
    public abstract void writeStockSummary(StockSummary stockSummary);
    public abstract void writeStockChartPoints(Set<ChartPoint> stockSummary);
    public abstract boolean changeSelectionStatus(String ticker);
}
