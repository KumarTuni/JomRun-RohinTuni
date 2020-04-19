package com.rohin.jomrun.ui.favorites;

import com.rohin.jomrun.model.data.Movie;
import com.rohin.jomrun.model.repositories.FavoritesRepository;
import com.rohin.jomrun.model.repositories.MoviesRepository;
import com.rohin.jomrun.model.repositories.network.search.SearchApiResponse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

public class FavoritesViewModel extends ViewModel {

    private FavoritesRepository favoritesRepository;
    private LiveData<PagedList<Movie>> moviesPagedListLvD;
    public FavoritesViewModel() {
        favoritesRepository = new FavoritesRepository();

        moviesPagedListLvD = favoritesRepository.getItemPagedList();
    }

    public LiveData<PagedList<Movie>> getMoviesPagedListLvD() {
        return moviesPagedListLvD;
    }

    public void updateMovie(Movie movie){
        favoritesRepository.updateMovie(movie);
    }

    public void loadInitial(){
        favoritesRepository.loadInitial();
    }

    public void reloadInitial(){
        favoritesRepository.reloadInitial();
    }



}