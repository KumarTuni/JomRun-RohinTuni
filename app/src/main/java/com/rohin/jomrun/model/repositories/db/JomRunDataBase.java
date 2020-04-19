package com.rohin.jomrun.model.repositories.db;

import android.app.Application;

import com.rohin.jomrun.model.data.Movie;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(
        exportSchema = false,
        entities = {
                Movie.class
        },
        version = 2
)
public abstract class JomRunDataBase extends RoomDatabase {

    private static JomRunDataBase INSTANCE;

    public static synchronized JomRunDataBase getInstance(Application application) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(application,
                    JomRunDataBase.class, "JomRunDataBase.db")
//                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build();

        return INSTANCE;
    }

    public abstract MoviesDao moviesDao();
}
