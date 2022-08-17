package com.example.demo.database.room;

import android.content.Context;

import com.example.demo.database.DBConfig;
import com.example.demo.database.room.entity.TrackLog;
import com.example.demo.database.room.entity.TrackLogDao;
import com.example.demo.database.room.entity.UserInfo;
import com.example.demo.database.room.entity.UserInfoDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        version = DBConfig.DB_VERSION,
        entities = {
                UserInfo.class,
                TrackLog.class
        },
        exportSchema = false
)
public abstract class AppDB extends RoomDatabase {

    public static AppDB create(Context appContext) {
        return Room.databaseBuilder(appContext, AppDB.class, DBConfig.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract UserInfoDao userDao();

    public abstract TrackLogDao trackLogDao();

}
