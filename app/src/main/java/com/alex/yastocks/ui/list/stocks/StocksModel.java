package com.alex.yastocks.ui.list.stocks;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.Stock;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class StocksModel {

    private final int ERROR_FLAG = 1;
    private final DBManager db;

    private final StocksViewModel viewModel;
    private final Retrofit retrofit;
    private Timer timer;

    private final Handler handler;


    StocksModel (StocksViewModel viewModel){
        this.viewModel = viewModel;
        retrofit = RetrofitInstance.getInstance();
        db = new RealmManager();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ERROR_FLAG){
                    viewModel.responseError("Request error");
                }
                if (msg.obj != null){
                    writeToRealm(((Stock)msg.obj));
                }else {
                    showStocksFromDB();
                }
            }
        };
    }

    public void startTimerRequestMostActiveStocks(){
        Log.e("TAG", "StocksModel startTimer");

        if (timer == null) timer = new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        //Тут отработка метода в отдельном потоке;
                        requestMostActiveStocks();
                    }
                    }
                , 0        // start delay
                , 10000); // 10 sec period;
    }

    public void stopTimerRequestMostActiveStocks(){
        Log.e("TAG", "StocksModel stopTimer");
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void requestMostActiveStocks() {

        Log.e("TAG", "StocksModel requestMostActiveStocks start");

        IStocks api = retrofit.create(IStocks.class);
        Call<ResponseBody> call = api.mostActives();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                String jsonResponse = "";

                if (response.isSuccessful()) {
                    try {
                        jsonResponse = response.body().string();

                        JSONObject fullRespond = new JSONObject(jsonResponse);
                        JSONArray quotes = fullRespond.getJSONArray("quotes");

                        for (int i = 0; i < fullRespond.getInt("count"); i++) {
                            final JSONObject quot = quotes.getJSONObject(i);

                            String ticker = quot.getString("symbol");
                            String companyName = quot.getString("longName");
                            String currency = quot.getString("currency");

                            double price = quot.getDouble("regularMarketPrice");
                            double priceChange = quot.getDouble("regularMarketChange");
                            double priceChangePercent = quot.getDouble("regularMarketChangePercent");


                            Stock stock = new Stock(ticker, companyName, price, priceChange, priceChangePercent, currency, false);
                            Message msg = new Message();
                            msg.obj = stock;
                            handler.sendMessage(msg);
                        }
                        //update UI
                        handler.sendEmptyMessage(0);
                        Log.e("TAG", "StocksModel requestMostActiveStocks end");
                    } catch (Exception e) {
                        handler.sendEmptyMessage(ERROR_FLAG);
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(ERROR_FLAG);
                    Log.e("TAG", "StocksModel request not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.sendEmptyMessage(ERROR_FLAG);
                Log.e("TAG", "StocksModel onFailure: " + t.getMessage());
            }
        });
    }

    public void showStocksFromDB(){
        ArrayList<Stock> stocksList = db.getFirst24Stocks();

        if (stocksList.size() > 0)
            viewModel.responseMostWatchedSuccess(stocksList);
    }

    private void writeToRealm(Stock stock){
        db.writeStock(stock);
    }

}
