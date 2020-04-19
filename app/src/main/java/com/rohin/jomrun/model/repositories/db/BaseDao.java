package com.rohin.jomrun.model.repositories.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

@Dao
public interface BaseDao<T> {

    @Insert
    void insert(T data);

    @Insert
    void insert(List<T> data);

    @Update
    void update(T data);
}
