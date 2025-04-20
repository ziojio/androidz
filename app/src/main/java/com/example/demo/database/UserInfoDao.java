package com.example.demo.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserInfo user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserInfo> users);

    @Update
    void update(UserInfo user);

    @Delete
    void delete(UserInfo user);

    @Query("DELETE FROM USERINFO WHERE id=:id")
    void deleteById(int id);

    @Query("DELETE FROM USERINFO")
    void deleteAll();

    @Query("DELETE FROM sqlite_sequence WHERE name = 'USERINFO'")
    void resetPrimaryKey();

    @Query("SELECT * FROM USERINFO WHERE id=:id")
    UserInfo getUser(int id);

    @Query("SELECT * FROM USERINFO")
    List<UserInfo> getAll();

    @Query("SELECT count() FROM USERINFO")
    int count();
}
