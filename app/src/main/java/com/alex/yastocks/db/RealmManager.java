package com.alex.yastocks.db;

import com.alex.yastocks.models.ChartPoint;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StockSummary;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmManager extends DBManager{


    private final RealmConfiguration config = new RealmConfiguration.Builder()
            .name("YaStocks.realm")
            .schemaVersion(2)
            .migration(new MyRealmMigration())
            .build();
    private final Realm realm = Realm.getInstance(config);

    private enum FLAGS {
        ticker,
        isSelected,
        companyName,
        interval,
        seconds,
    }

    @Override
    public ArrayList<Stock> getSelectedStocks() {
        RealmResults<Stock> results = realm.where(Stock.class)
                .equalTo(FLAGS.isSelected.name(), true)
                .findAllAsync();
        return new ArrayList<>(results);
    }

    @Override
    public TreeSet<ChartPoint> getStockChartPoints(String ticker, String interval, int startTime) {
        RealmResults<ChartPoint> results = realm.where(ChartPoint.class)
                .equalTo(FLAGS.ticker.name(), ticker)
                .and()
                .equalTo(FLAGS.interval.name(), interval)
                .and()
                .greaterThanOrEqualTo(FLAGS.seconds.name(), startTime)
                .findAllAsync();
        return new TreeSet<>(results);
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
                    .like(FLAGS.companyName.name(), name, Case.INSENSITIVE)
                    .or()
                    .like(FLAGS.ticker.name(), name, Case.INSENSITIVE)
                    .sort(FLAGS.companyName.name(), Sort.ASCENDING)
                .findAllAsync()
        );
    }

    @Override
    public void writeStock(Stock stock) {
        realm.executeTransactionAsync(t->{
            // save stocks selection status, if exist
            Stock result = t.where(Stock.class)
                    .equalTo(FLAGS.ticker.name(), stock.getTicker())
                    .findFirst();
            if (result != null) stock.setSelected(result.isSelected());

            t.insertOrUpdate(stock);
        });
    }

    @Override
    public void writeStockSummary(StockSummary stockSummary) {
        realm.executeTransactionAsync(t->{
            t.insertOrUpdate(stockSummary);
        });
    }

    @Override
    public void writeStockChartPoints(Set<ChartPoint> stockSummary) {
        realm.executeTransactionAsync(t->{
            t.insertOrUpdate(stockSummary);
        });
    }

    @Override
    public Stock getStock(String ticker) {
        return realm.where(Stock.class)
                .equalTo(FLAGS.ticker.name(), ticker)
                .findFirst();
    }

    @Override
    public StockSummary getStockSummary(String ticker) {
        return realm.where(StockSummary.class)
                .equalTo(FLAGS.ticker.name(), ticker)
                .findFirst();
    }

    @Override
    public boolean changeSelectionStatus(String ticker) {
        Stock s = getStock(ticker);
        realm.executeTransactionAsync(t -> {

            Stock stock = t.where(Stock.class)
                    .equalTo(FLAGS.ticker.name(), ticker)
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
