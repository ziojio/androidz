package com.example.demo.database.room.entity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TrackLogDao {

    @Query("select * from TrackLog")
    List<TrackLog> queryAll();

    @Query("select * from TrackLog where id=:id")
    TrackLog queryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TrackLog> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TrackLog trackLog);

    @Update
    void update(TrackLog trackLog);

    @Delete
    void delete(TrackLog trackLog);

}
