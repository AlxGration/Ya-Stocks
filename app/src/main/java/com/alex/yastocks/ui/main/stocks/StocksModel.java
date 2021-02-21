package com.alex.yastocks.ui.main.stocks;

import android.os.Handler;
import android.util.Log;

import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.models.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class StocksModel {

    private final String REQUEST_ERR_MSG = "Ошибка запроса";
    private final Realm realm;

    private final StocksViewModel viewModel;
    private final Retrofit retrofit;
    private Timer timer;

    private final Handler handler;


    StocksModel (StocksViewModel viewModel){
        this.viewModel = viewModel;
        retrofit = RetrofitInstance.getInstance();
        realm = Realm.getDefaultInstance();

        handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                showStocksFromDB();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                        viewModel.responseError(REQUEST_ERR_MSG);
                        return;
                    }

                    try {
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

                            realm.executeTransactionAsync(r -> {
                                Stock stock = new Stock();
                                stock.setTicker(ticker);

                                stock.setCompanyName(companyName);
                                stock.setCurrency(currency);

                                stock.setPrice(price);
                                stock.setPriceChange(priceChange);
                                stock.setPriceChangePercent(priceChangePercent);

                                r.insertOrUpdate(stock);
                            });
                        }
                        //update UI
                        handler.sendEmptyMessage(0);
                        Log.e("TAG", "StocksModel requestMostActiveStocks end");
                        //Log.e("TAG", "json MostActives : "+ jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("TAG", "StocksModel request not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "StocksModel onFailure: " + t.getMessage());
            }
        });
    }

    public void showStocksFromDB(){
        ArrayList<Stock> stocksList = new ArrayList<>();

        RealmResults<Stock> results = realm.where(Stock.class)
                .limit(24)
                .findAllAsync();

        for(Stock stock: results) {
            Log.e("TAG", "From Realm: "+stock.toString());
            stocksList.add(stock);
        }

        if (stocksList.size() > 0)
            viewModel.responseMostWatchedSuccess(stocksList);
    }

}
