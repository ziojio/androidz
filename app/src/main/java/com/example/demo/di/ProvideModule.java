package com.example.demo.di;

import com.example.demo.UIApp;
import com.example.demo.database.AppRoomDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ProvideModule {
    @Provides
    UIApp application() {
        return UIApp.getApp();
    }

    @Singleton
    @Provides
    AppRoomDatabase database() {
        return UIApp.getDB();
    }
}
