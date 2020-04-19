package com.rohin.jomrun.model.repositories.network.search;

import com.rohin.jomrun.model.data.Movie;

import java.util.List;

import retrofit2.Call;

public abstract class NetworkCallback {

    public abstract void saveToDb(List<Movie> movies);

    public abstract Call<SearchApiResponse> createCall();

    public abstract void onError(String error);


}
