package com.example.demo.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(version = AppRoomDatabase.DB_VERSION, exportSchema = false,
        entities = {UserInfo.class, TrackLog.class})
public abstract class AppRoomDatabase extends RoomDatabase {
    public static final String DB_NAME = "test";
    public static final int DB_VERSION = 3;

    public static AppRoomDatabase create(Context appContext) {
        return Room.databaseBuilder(appContext, AppRoomDatabase.class, DB_NAME)
                .fallbackToDestructiveMigrationOnDowngrade(true)
                .build();
    }

    public abstract UserInfoDao userDao();

    public abstract TrackLogDao trackLogDao();

}
