package com.alex.yastocks.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public abstract class RetrofitInstance {

    //TODO:: uncomment for use Api_Key

    private static final String API_KEY = "NM1l64ppmlniWG8Oy4KpvYH1lnVEUypg0nevOmvqN4gityGaInE1nOZ5r7Bd";
    //Header key:  X-Mboum-Secret:

    private static final String BASE_URL = "https://mboum.com/api/v1/";

    private static Retrofit retrofit;

    private static OkHttpClient.Builder getHttpClient(){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("X-Mboum-Secret", API_KEY)
                        .build();

                return chain.proceed(request);
            }
        });
        return client;
    }

    public static Retrofit getInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofit;
    }
}
