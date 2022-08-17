package com.example.demo.database.room.entity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserInfoDao {

    @Query("SELECT * FROM UserInfo")
    List<UserInfo> getAll();

    @Query("SELECT count() FROM UserInfo")
    int count();

    @Query("SELECT * FROM UserInfo WHERE id=:id")
    UserInfo getUser(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserInfo> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfo user);

    @Update
    void update(UserInfo user);

    @Delete
    void delete(UserInfo user);

    @Query("DELETE FROM UserInfo")
    void deleteAll();
}
