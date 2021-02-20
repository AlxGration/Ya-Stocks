package com.alex.yastocks.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface IStocks {

    @GET("co/collections")
    Call<ResponseBody> getCollections();

    @GET("tr/trending")
    Call<ResponseBody> mostWatched();

    @GET("co/collections/?list=most_actives&start=1&apikey=demo")
    Call<ResponseBody> mostActives();

}
