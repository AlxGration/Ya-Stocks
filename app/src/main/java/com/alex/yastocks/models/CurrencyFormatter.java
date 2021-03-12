package com.alex.yastocks.models;

import android.util.Log;

import java.text.DecimalFormat;

public class CurrencyFormatter {

    public static String format(String currency, double price){
        if (currency == null) return "format error";
        switch (currency){
            case "USD":
                return '$'+priceToString(price);

            case "RU":
                return priceToString(price)+" ₽";
        }
        return "add currency";
    }

    public static String formatWithPercent(String currency, double price, double percent){
        if (currency == null) return "format error";

        char sign = ' ';
        if (price > 0) sign = '+';
        else if (price < 0) sign = '-';

        switch (currency){
            case "USD":
                return sign + "$"+priceToString(Math.abs(price)) + " (" + String.format("%.2f", Math.abs(percent))+"%)";

            case "RU":
                return sign + priceToString(Math.abs(price))+" ₽" + " (" + String.format("%.2f", Math.abs(percent))+"%)";
        }
        return "add currency";
    }

    private static String priceWithDecimal (Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(price);
    }

    private static String priceWithoutDecimal (Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###.##");
        return formatter.format(price);
    }

    private static String priceToString(Double price) {
        String toShow = priceWithoutDecimal(price);
        return toShow;
        /*
        if (toShow.indexOf(".") > 0) {
            return priceWithDecimal(price);
        } else {
            return toShow;
        }

         */
    }
}
