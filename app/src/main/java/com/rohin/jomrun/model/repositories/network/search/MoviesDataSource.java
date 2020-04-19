package com.rohin.jomrun.model.repositories.network.search;

import com.rohin.jomrun.Consts;
import com.rohin.jomrun.JomRunApplication;
import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.model.repositories.db.JomRunDataBase;
import com.rohin.jomrun.model.repositories.network.RetrofitClient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDataSource extends PageKeyedDataSource<Integer, Movie> {
    private MutableLiveData<SearchApiResponse> apiResponseMutableLiveData;
    private NetworkCallback networkCallback;
    private LoadParams<Integer> loadParams;
    private LoadInitialCallback<Integer, Movie> initialCallback;
    private LoadCallback<Integer, Movie> afterCallback;
    private LoadInitialParams<Integer> integerLoadInitialParams;
    private int totalLoaded;

    public MoviesDataSource(NetworkCallback networkCallback, MutableLiveData<SearchApiResponse> apiResponseMutableLiveData) {
        this.networkCallback = networkCallback;
        this.totalLoaded = 0;
        this.apiResponseMutableLiveData = apiResponseMutableLiveData;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        this.integerLoadInitialParams = params;
        this.initialCallback = callback;
        apiResponseMutableLiveData.postValue(SearchApiResponse.loading(null));
        Call<SearchApiResponse> call = networkCallback.createCall();
        if (call != null) {
            call.enqueue(new Callback<SearchApiResponse>() {
                @Override
                public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                    if (response.body() == null || !response.isSuccessful()) {
                        apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, response.message()));
                    } else {
                        if (response.body().isResponse()) { // success
                            networkCallback.saveToDb(response.body().getMovies());
                            apiResponseMutableLiveData.postValue(SearchApiResponse.success(response.body()));
                            totalLoaded += response.body().getMovies().size();
                            if (response.body().getMovies() == null)
                                response.body().setMovies(new ArrayList<>());
                            callback.onResult(response.body().getMovies(), 0, response.body().getTotalResults(), null, 2);
                        } else {//failure
                            apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, response.body().getErrorMsg()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                    apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, t.getMessage()));
                }
            });
        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        this.loadParams = params;
        this.afterCallback = callback;
        apiResponseMutableLiveData.postValue(SearchApiResponse.loading(null));
        Call<SearchApiResponse> call = networkCallback.createCall();
        if (call != null) {
            call.enqueue(new Callback<SearchApiResponse>() {
                @Override
                public void onResponse(Call<SearchApiResponse> call, Response<SearchApiResponse> response) {
                    if (response.body() == null || !response.isSuccessful()) {
                        apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, response.message()));
                    } else {
                        if (response.body().isResponse()) { // success
                            networkCallback.saveToDb(response.body().getMovies());

                            totalLoaded += response.body().getMovies().size();
                            apiResponseMutableLiveData.postValue(SearchApiResponse.success(response.body()));
                            if (response.body().getMovies() == null)
                                response.body().setMovies(new ArrayList<>());
                            callback.onResult(response.body().getMovies(), totalLoaded >= response.body().getTotalResults() ? null : 1);
                        } else {//failure
                            apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, response.body().getErrorMsg()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchApiResponse> call, Throwable t) {
                    apiResponseMutableLiveData.postValue(SearchApiResponse.error(null, t.getMessage()));
                }
            });
        }


    }

    public void reloadInitial() {
        loadInitial(integerLoadInitialParams, initialCallback);
    }

    public void reloadAfter() {
        loadAfter(loadParams, afterCallback);

    }
}
