package com.alex.yastocks.models;

public enum ChartInterval {
    DAY("5m"),
    WEEK("30m"),
    MONTH("1d"),
    YEAR("1wk");

    private String interval;
    ChartInterval(String interval){
        this.interval = interval;
    }
    public String getInterval(){return interval;}
}
