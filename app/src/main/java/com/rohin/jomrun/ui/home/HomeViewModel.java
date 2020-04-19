package com.rohin.jomrun.ui.home;

import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.model.repositories.MoviesRepository;
import com.rohin.jomrun.model.repositories.network.search.SearchApiResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

public class HomeViewModel extends ViewModel {

    private MoviesRepository moviesRepository;
    private LiveData<PagedList<Movie>> moviesPagedListLvD;
    private LiveData<SearchApiResponse> apiResponseLiveData;

    public HomeViewModel() {
        moviesRepository = new MoviesRepository();

        apiResponseLiveData = moviesRepository.getApiResponseMutableLiveData();
        moviesPagedListLvD = moviesRepository.getItemPagedList();
    }

    public LiveData<PagedList<Movie>> getMoviesPagedListLvD() {
        return moviesPagedListLvD;
    }



    public LiveData<SearchApiResponse> getApiResponseLiveData() {
        return apiResponseLiveData;
    }

    public void updateMovie(Movie movie){
        moviesRepository.updateMovie(movie);
    }

    public void loadInitial(String q,String type){
        moviesRepository.loadInitial(q,type);
    }

    public void reloadInitial(){
        moviesRepository.reloadInitial();
    }

    public void reloadCurrentPage(){
        moviesRepository.reloadCurrentPage();
    }

    public int getCurrentPageIdx(){
        return moviesRepository.getCurrentPageIdx();
    }


}