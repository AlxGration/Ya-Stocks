package com.alex.yastocks.ui.stock;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alex.yastocks.api.IStocks;
import com.alex.yastocks.api.RetrofitInstance;
import com.alex.yastocks.db.DBManager;
import com.alex.yastocks.db.RealmManager;
import com.alex.yastocks.models.Stock;
import com.alex.yastocks.models.StockSummary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class InfoModel {

    private final InfoViewModel viewModel;
    private final DBManager db;


    InfoModel(InfoViewModel viewModel){
        this.viewModel = viewModel;
        db = new RealmManager();
    }

    public void changeSelectionStatus(String ticker){

        viewModel.selectionChanged(
                db.changeSelectionStatus(ticker)
        );
    }

}
