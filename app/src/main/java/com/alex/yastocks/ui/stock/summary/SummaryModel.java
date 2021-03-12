package com.alex.yastocks.ui.stock.summary;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.StockSummary;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SummaryModel {

    private final DBManager db;
    private final SummaryViewModel viewModel;
    private final Retrofit retrofit;
    private final Handler handler;
    private final int ERROR_FLAG = 1;

    SummaryModel(SummaryViewModel viewModel){
        db = new RealmManager();
        this.viewModel = viewModel;
        retrofit = RetrofitInstance.getInstance();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == ERROR_FLAG){
                    viewModel.responseError("Request error");
                }
                if (msg.obj != null){
                    writeToRealm((StockSummary)msg.obj);
                    viewModel.responseSuccess();
                    showSummaryData((StockSummary)msg.obj);
                }
            }
        };
    }

    public void requestSummary(String ticker){
        StockSummary stockSummary = db.getStockSummary(ticker);
        if (stockSummary != null) {
            viewModel.responseSuccess();
            showSummaryData(stockSummary);
        }
        else downloadStockSummary(ticker);
    }

    private void showSummaryData(StockSummary stockSummary){
        viewModel.setCompanySummary(stockSummary.getLongBusinessSummary());
        viewModel.setCountry(stockSummary.getCountry());
        viewModel.setSector(stockSummary.getSector());
    }

    private void downloadStockSummary(String ticker){
        IStocks api = retrofit.create(IStocks.class);
        //TODO: uncomment for debug
        //Call<ResponseBody> call = api.summary(ticker, "demo");
        Call<ResponseBody> call = api.summary(ticker);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String jsonResponse = "";

                Log.e("TAG","request "+call.request().toString());

                if (response.isSuccessful()) {
                    try {
                        jsonResponse = response.body().string();

                        JSONObject fullRespond = new JSONObject(jsonResponse);
                        String longBusinessSummary = fullRespond.getString("longBusinessSummary");
                        String sector = fullRespond.getString("sector");
                        String country = fullRespond.getString("country");

                        StockSummary stockSummary = new StockSummary(ticker, longBusinessSummary, sector, country);
                        Message msg = new Message();
                        msg.obj = stockSummary;
                        handler.sendMessage(msg);
                    } catch (Exception e) {
                        handler.sendEmptyMessage(ERROR_FLAG);
                        e.printStackTrace();
                    }
                } else {
                    handler.sendEmptyMessage(ERROR_FLAG);
                    Log.e("TAG", "SummaryModel request not success");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handler.sendEmptyMessage(ERROR_FLAG);
                Log.e("TAG", "SummaryModel onFailure: " + t.getMessage());
            }
        });
    }

    private void writeToRealm(StockSummary stockSummary){
        db.writeStockSummary(stockSummary);
    }
}
