package com.rohin.jomrun.model.repositories.network.search;

import com.rohin.jomrun.model.data.Movie;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class MoviesDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<SearchApiResponse> apiResponseMutableLiveData;
    private MutableLiveData<MoviesDataSource> sourceMutableLiveData;
    private NetworkCallback networkCallback;

    public MoviesDataSourceFactory(NetworkCallback networkCallback, MutableLiveData<SearchApiResponse> apiResponseMutableLiveData) {
        this.apiResponseMutableLiveData = apiResponseMutableLiveData;
        this.networkCallback = networkCallback;
        this.sourceMutableLiveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Movie> create() {
        MoviesDataSource itemDataSource = new MoviesDataSource(networkCallback, apiResponseMutableLiveData);
        sourceMutableLiveData.postValue(itemDataSource);

        return itemDataSource;
    }

    public MutableLiveData<MoviesDataSource> getSourceMutableLiveData() {
        return sourceMutableLiveData;
    }
}
