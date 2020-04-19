package com.rohin.jomrun.model.repositories;

import android.text.TextUtils;
import android.widget.SearchView;

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

public class MoviesRepository {
    private final JomRunDataBase database;

    private MutableLiveData<SearchApiResponse> apiResponseMutableLiveData;
    private PagedList<Movie> itemPagedListLocale;
    private LiveData<PagedList<Movie>> itemPagedList;
    private LiveData<MoviesDataSource> itemDataSource;

    private MoviesDataSourceFactory factory;
    private DataSource.Factory<Integer, Movie> factoryLocale;
    private PagedList.Config config;
    private MediatorLiveData<PagedList<Movie>> liveDataMerger;

    private NetworkCallback networkCallback;

    private String q; //q
    private String type;
    private int page = 0;

    public MoviesRepository() {
        database = JomRunDataBase.getInstance(JomRunApplication.getInstance());
        apiResponseMutableLiveData = new MutableLiveData<>();
        liveDataMerger = new MediatorLiveData<>();
        networkCallback = new NetworkCallback() {
            @Override
            public void saveToDb(List<Movie> movies) {
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> database.moviesDao().upsert(movies));

                page++;
            }

            @Override
            public Call<SearchApiResponse> createCall() {
                return RetrofitClient.getInstance()
                        .getApi()
                        .search(Consts.API_KEY,
                                q,
                                type,
                                page);
            }

            @Override
            public void onError(String error) {
                loadLocale(error);

            }
        };
        factory = new MoviesDataSourceFactory(networkCallback, apiResponseMutableLiveData);
        itemDataSource = factory.getSourceMutableLiveData();
        config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(1)
                .setInitialLoadSizeHint(Consts.PAGE_SIZE)
                .setPageSize(Consts.PAGE_SIZE)
                .build();

        Executor executor = Executors.newSingleThreadExecutor();
        itemPagedList = new LivePagedListBuilder<>(factory, config)
                .setFetchExecutor(executor)
                .setInitialLoadKey(1)
                .setBoundaryCallback(new PagedList.BoundaryCallback<Movie>() {
                    @Override
                    public void onZeroItemsLoaded() {
                        super.onZeroItemsLoaded();
                        apiResponseMutableLiveData.setValue(SearchApiResponse.empty(new ArrayList<>()));
                    }

                    @Override
                    public void onItemAtEndLoaded(@NonNull Movie itemAtEnd) {
                        super.onItemAtEndLoaded(itemAtEnd);
                    }

                    @Override
                    public void onItemAtFrontLoaded(@NonNull Movie itemAtFront) {
                        super.onItemAtFrontLoaded(itemAtFront);
                    }
                }).build();

        liveDataMerger.addSource(itemPagedList, movies ->
                liveDataMerger.setValue(movies));

    }

    private void loadLocale(String errMsg) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            factoryLocale = database.moviesDao().loadPage(q != null ? "%" + q + "%" : null);
            itemPagedListLocale = new PagedList.Builder<>(factoryLocale.create(), config)
                    .setInitialKey(0)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .build();
            liveDataMerger.postValue(itemPagedListLocale);
            apiResponseMutableLiveData.postValue(SearchApiResponse.error(new ArrayList<>(), ""));

        });
    }

    public LiveData<PagedList<Movie>> getItemPagedList() {
        return liveDataMerger;
    }

    public MutableLiveData<SearchApiResponse> getApiResponseMutableLiveData() {
        return apiResponseMutableLiveData;
    }

    public void loadInitial(String q, String type) {
        this.type = type;
        this.q = q;
        if (TextUtils.isEmpty(q))
            page = 0;
        else
            page = 1;
        reloadInitial();

    }

    public int getCurrentPageIdx() {
        return page;
    }

    public void reloadInitial() {
        if (this.page == 0)
            return;
        this.page = 1;
        Objects.requireNonNull(itemPagedList.getValue()).getDataSource().invalidate();
    }

    public void reloadCurrentPage() {
        Objects.requireNonNull(itemDataSource.getValue()).reloadAfter();

    }

    public void updateMovie(Movie movie) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> database.moviesDao().update(movie));
    }

}
