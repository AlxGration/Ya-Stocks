package com.alex.yastocks.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class StockSummary extends RealmObject {

    @PrimaryKey
    String ticker;
    String longBusinessSummary;
    String sector;
    String country;

    public StockSummary(){

    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public StockSummary(String ticker, String longBusinessSummary, String sector, String country) {
        this.ticker = ticker;
        this.longBusinessSummary = longBusinessSummary;
        this.sector = sector;
        this.country = country;
    }

    public String getLongBusinessSummary() {
        return longBusinessSummary;
    }

    public void setLongBusinessSummary(String longBusinessSummary) {
        this.longBusinessSummary = longBusinessSummary;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
