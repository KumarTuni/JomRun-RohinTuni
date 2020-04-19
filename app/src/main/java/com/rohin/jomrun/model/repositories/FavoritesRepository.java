package com.rohin.jomrun.model.repositories;

import android.text.TextUtils;

import com.rohin.jomrun.Consts;
import com.rohin.jomrun.JomRunApplication;
import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.model.repositories.db.JomRunDataBase;
import com.rohin.jomrun.model.repositories.network.RetrofitClient;
import com.rohin.jomrun.model.repositories.network.search.MoviesDataSource;
import com.rohin.jomrun.model.repositories.network.search.MoviesDataSourceFactory;
import com.rohin.jomrun.model.repositories.network.search.NetworkCallback;
import com.rohin.jomrun.model.repositories.network.search.SearchApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import retrofit2.Call;

public class FavoritesRepository {
    private final JomRunDataBase database;

    private PagedList<Movie> itemPagedListLocale;
    private DataSource.Factory<Integer, Movie> factoryLocale;
    private PagedList.Config config;
    private MediatorLiveData<PagedList<Movie>> liveDataMerger;

    public FavoritesRepository() {
        database = JomRunDataBase.getInstance(JomRunApplication.getInstance());
        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(1)
                .setInitialLoadSizeHint(Consts.PAGE_SIZE)
                .setPageSize(Consts.PAGE_SIZE)
                .build();
        liveDataMerger = new MediatorLiveData<>();
        loadLocale("");

    }

    private void loadLocale(String errMsg) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            factoryLocale = database.moviesDao().loadFavoritePage();
            itemPagedListLocale = new PagedList.Builder<>(factoryLocale.create(), config)
                    .setInitialKey(0)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .build();
            liveDataMerger.postValue(itemPagedListLocale);


        });
    }

    public LiveData<PagedList<Movie>> getItemPagedList() {
        return liveDataMerger;
    }

    public void loadInitial() {

        reloadInitial();

    }


    public void reloadInitial() {
        Objects.requireNonNull(itemPagedListLocale).getDataSource().invalidate();
    }


    public void updateMovie(Movie movie) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> database.moviesDao().update(movie));
    }

}
