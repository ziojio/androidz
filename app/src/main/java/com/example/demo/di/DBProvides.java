package com.example.demo.di;

import com.example.demo.UIApp;
import com.example.demo.database.room.AppDB;
import com.example.demo.database.room.entity.TrackLogDao;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public interface DBProvides {

    @Provides
    static AppDB DB() {
        return UIApp.getDB();
    }

    @Provides
    static TrackLogDao trackLogDao() {
        return UIApp.getDB().trackLogDao();
    }
}
