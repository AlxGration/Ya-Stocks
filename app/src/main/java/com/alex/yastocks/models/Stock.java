package com.alex.yastocks.models;

import androidx.annotation.NonNull;

import com.alex.yastocks.R;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Stock  extends RealmObject {

    @PrimaryKey
    private String ticker;
    private String companyName;
    private String currency;

    private boolean isSelected;

    private int imgStock;

    private double price;
    private double priceChange;
    private double priceChangePercent;

    public Stock(){
        ticker = "";
        companyName = "";
        isSelected = false;
        imgStock = R.drawable.ic_img_placeholder;
        price = 0;
        priceChange = 0;
        priceChangePercent = 0;
        currency = "RU";

    }

    public Stock(String ticker, String companyName, double price, double priceChange, double priceChangePercent, String currency, boolean isSelected){
        imgStock = R.drawable.ic_img_placeholder;
        this.ticker = ticker;
        this.companyName = companyName;
        this.price = price;
        this.priceChange = priceChange;
        this.isSelected = isSelected;
        this.currency = currency;
        this.priceChangePercent = priceChangePercent;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getStockImg() {
        return imgStock;
    }

    public void setStockImg(int imgStock) {
        this.imgStock = imgStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPriceChange() {
        return priceChange;
    }

    public void setPriceChange(double priceChange) {
        this.priceChange = priceChange;
    }

    public double getPriceChangePercent() {
        return priceChangePercent;
    }

    public void setPriceChangePercent(double priceChangePercent) {
        this.priceChangePercent = priceChangePercent;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @NonNull
    @Override
    public String toString() {
        return "Stock "+ ticker+" "+companyName+" "+currency+" "+price+" "+priceChange;
    }
}
