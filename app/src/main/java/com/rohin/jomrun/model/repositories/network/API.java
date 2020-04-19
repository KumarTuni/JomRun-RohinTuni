package com.rohin.jomrun.model.repositories.network;

import com.rohin.jomrun.model.repositories.network.search.SearchApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {

    @GET(".")
    Call<SearchApiResponse> search(
            @Query("apikey") String apiKey,
            @Query("s") String q,
            @Query("type") String type,
            @Query("page") int page
    );


}
