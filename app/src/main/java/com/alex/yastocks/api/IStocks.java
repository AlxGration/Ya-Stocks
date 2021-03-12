package com.alex.yastocks.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IStocks {

    //@GET("co/collections/?list=most_actives&start=1&apikey=demo")
    @GET("co/collections/?list=most_actives&start=1")
    Call<ResponseBody> mostActives();

    @GET("qu/quote/profile/")
    Call<ResponseBody> summary(
    	@Query("symbol")String symbol
    	//,@Query("apikey") String token
    );


    @GET("hi/history/")
    Call<ResponseBody> historicalData(
    	@Query("symbol")String symbol,
    	@Query("interval") String interval,//15m for DAY, 
    	@Query("diffandsplits") boolean diffAndSplits
            //,@Query("apikey") String token
     );

}
