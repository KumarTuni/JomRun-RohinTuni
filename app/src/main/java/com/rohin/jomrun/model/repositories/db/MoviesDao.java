package com.rohin.jomrun.model.repositories.db;

import android.database.sqlite.SQLiteConstraintException;

import com.rohin.jomrun.model.data.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public abstract class MoviesDao implements BaseDao<Movie> {

    @Query("SELECT id,title,year,poster,type,favorite FROM movies WHERE title LIKE :q ")
    public abstract DataSource.Factory<Integer, Movie> loadPage(String q);

    @Query("SELECT id,title,year,poster,type,favorite FROM movies WHERE favorite = 1 ")
    public abstract DataSource.Factory<Integer, Movie> loadFavoritePage();


    @Query("SELECT * FROM movies WHERE id = :id")
    public abstract LiveData<Movie> loadMovieLive(String id);

    @Query("SELECT * FROM movies WHERE id = :id")
    public abstract Movie loadMovie(String id);

    public void upsert(Movie movie){
        try{
            insert(movie);
        }catch (SQLiteConstraintException e){
            Movie temp = loadMovie(movie.getId());

            movie.setFavorite(temp.isFavorite());
            update(movie);
        }
    }

    public void upsert(List<Movie> movies){
        for (Movie movie:movies){
            upsert(movie);
        }
    }

}
