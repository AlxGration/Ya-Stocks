package com.alex.yastocks.ui.stocks;

import android.util.Log;

import com.alex.yastocks.R;
import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.models.Stock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

class StocksModel {

    private final String REQUEST_ERR_MSG = "Ошибка запроса";

    private StocksViewModel viewModel;
    private Retrofit retrofit;

    StocksModel (StocksViewModel viewModel){
        this.viewModel = viewModel;
        retrofit = RetrofitInstance.getInstance();
    }

    public void mostActiveStocks() {

        IStocks api = retrofit.create(IStocks.class);
        Call<ResponseBody> call = api.mostActives();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                ArrayList<Stock> stocksList;
                String jsonResponse = "";

                if (response.isSuccessful()){
                    try {
                        jsonResponse = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                        viewModel.requestError(REQUEST_ERR_MSG);
                        return;
                    }

                    stocksList = new ArrayList<>();
                    try {
                        JSONObject fullRespond = new JSONObject(jsonResponse);
                        JSONArray quotes = fullRespond.getJSONArray("quotes");

                        for(int i = 0; i < fullRespond.getInt("count"); i++){
                            JSONObject quot = quotes.getJSONObject(i);

                            Stock stock = new Stock();
                            stock.setTicker(quot.getString("symbol"));
                            stock.setCompanyName(quot.getString("longName"));

                            stock.setCurrency(quot.getString("currency"));
                            stock.setPrice(quot.getDouble("regularMarketPrice"));
                            stock.setPriceChange(quot.getDouble("regularMarketChange"));
                            stock.setPriceChangePercent(quot.getDouble("regularMarketChangePercent"));


                            stocksList.add(stock);

                            Log.e("TAG", "adding "+stock.toString());
                        }
                        //Log.e("TAG", "json MostActives : "+ jsonResponse);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (stocksList.size() > 0)
                        viewModel.requestMostWatchedSuccess(stocksList);
                    else
                        viewModel.requestError(REQUEST_ERR_MSG);

                }else{
                    viewModel.requestError(REQUEST_ERR_MSG);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                viewModel.requestError( REQUEST_ERR_MSG +" "+ t.getMessage());
            }
        });
    }
}
