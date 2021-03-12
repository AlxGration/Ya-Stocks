package com.alex.yastocks.ui.stock.chart;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.ChartPoint;
import com.alex.yastocks.models.CurrencyFormatter;
import com.alex.yastocks.models.Stock;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChartModel {

    private final DBManager db;
    private final ChartViewModel viewModel;
    private final Retrofit retrofit;
    private final Handler handler;
    private final int ERROR_FLAG = 1;

    ChartModel(ChartViewModel viewModel){
        db = new RealmManager();
        this.viewModel = viewModel;
        retrofit = RetrofitInstance.getInstance();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ERROR_FLAG){
                    viewModel.responseError("Request error");
                }
                if (msg.obj != null){
                    writeToRealm((TreeSet<ChartPoint>)msg.obj);
                    viewModel.responseSuccess();
                    showChartData((TreeSet<ChartPoint>)msg.obj, msg.what);//"what" is startTime
                }
            }
        };
    }


    // Достает данные из БД, если они есть и отправляет запрос на сервер для их обновления
    public void requestChartData(String ticker, String interval, int startTime){
        Stock stock = db.getStock(ticker);
        viewModel.setPrice(CurrencyFormatter.format(stock.getCurrency(), stock.getPrice()));

        TreeSet<ChartPoint> points = db.getStockChartPoints(ticker, interval, startTime);
        if (points.size()>0) {
            viewModel.responseSuccess();
            showChartData(points, startTime);
        }
        downloadStockChart(ticker, interval, startTime);
    }


    private void showChartData(TreeSet<ChartPoint> points, int startTime){
        List<Entry> entries = new ArrayList<>(points.size());

        // price changing during period (day, week, month, year)
            ChartPoint oldest = points.first();
            ChartPoint last = points.last();
            float difference = oldest.getOpen() - last.getOpen();
            float percent = last.getOpen() * 100 / oldest.getOpen() - 100;

            viewModel.setPriceChange(
                    CurrencyFormatter.formatWithPercent("USD", difference, percent)
            );
        /////////////////////////////////////////////////////////

        for(ChartPoint cp: points){
            //this comparison needs 'cause data could come from request(without date cutting)
            if (cp.getSeconds() > startTime)
                entries.add(new Entry(cp.getSeconds(), cp.getOpen()));
        }
        viewModel.setChartDataEntries(entries);
    }

    private void downloadStockChart(String ticker, String interval, int startTime){
        IStocks api = retrofit.create(IStocks.class);
        //TODO: uncomment for debug
        //Call<ResponseBody> call = api.historicalData(ticker, interval, true, "demo");
        Call<ResponseBody> call = api.historicalData(ticker, interval, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonResponse = "";

                Log.e("TAG","request "+call.request().toString());

                if (response.isSuccessful()) {
                    try {
                        jsonResponse = response.body().string();

                        JSONObject fullRespond = new JSONObject(jsonResponse);
                        JSONObject items = fullRespond.getJSONObject("items");
                        Iterator<String> iter = items.keys();
                        JSONObject data;
                        TreeSet<ChartPoint> dots = new TreeSet<>();
                        while(iter.hasNext()){
                            String keySeconds = iter.next();
                            data = items.getJSONObject(keySeconds);


                            float close = (float) data.getDouble("close");
                            float open = (float) data.getDouble("open");
                            float high = (float) data.getDouble("high");
                            float low = (float) data.getDouble("low");

                            dots.add(new ChartPoint(
                                    ticker,
                                    interval,
                                    Long.parseLong(keySeconds),
                                    open,
                                    high,
                                    low,
                                    close
                            ));
                        }

                        Message msg = new Message();
                        msg.obj = dots;
                        msg.what = startTime;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        handler.sendEmptyMessage(ERROR_FLAG);
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(ERROR_FLAG);
                    Log.e("TAG", "ChartModel request not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.sendEmptyMessage(ERROR_FLAG);
                Log.e("TAG", "ChartModel onFailure: " + t.getMessage());
            }
        });
    }

    private void writeToRealm(Set<ChartPoint> points){
        db.writeStockChartPoints(points);
    }
}
