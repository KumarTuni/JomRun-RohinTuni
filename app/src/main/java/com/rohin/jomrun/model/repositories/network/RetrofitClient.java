package com.rohin.jomrun.model.repositories.network;

import com.rohin.jomrun.Consts;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static RetrofitClient instance;
    private Retrofit retrofit;
    public static synchronized RetrofitClient getInstance() {
        if (instance == null)
            instance = new RetrofitClient();
        return instance;
    }

    public RetrofitClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.followRedirects(false);

//        builder.dns(new MakaniDns());
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60,TimeUnit.SECONDS);
        builder.writeTimeout(120,TimeUnit.SECONDS);
        OkHttpClient httpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Consts.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public API getApi(){
        return retrofit.create(API.class);
    }

}
