package com.alex.yastocks.models;

import java.util.Objects;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ChartPoint extends RealmObject implements Comparable<ChartPoint>{

    @PrimaryKey
    private String id;

    private String ticker;//F
    private String interval;//15m
    private long seconds;//612362600
    private float open;//10.86
    private float high;//11.06
    private float low;//10.84
    private float close;//11.02

    public ChartPoint(){}

    public ChartPoint(String ticker, String interval, long seconds, float open, float high, float low, float close) {
        id = ticker+" "+seconds;
        this.ticker = ticker;
        this.interval = interval;
        this.seconds = seconds;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
    }

    public String getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChartPoint that = (ChartPoint) o;
        return seconds == that.seconds &&
                Float.compare(that.open, open) == 0 &&
                Float.compare(that.high, high) == 0 &&
                Float.compare(that.low, low) == 0 &&
                Float.compare(that.close, close) == 0 &&
                Objects.equals(ticker, that.ticker) &&
                Objects.equals(interval, that.interval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seconds);
    }

    @Override
    public int compareTo(ChartPoint chartPoint) {
        return (int)(seconds - chartPoint.seconds);
    }
}
