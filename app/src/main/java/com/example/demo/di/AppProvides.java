package com.example.demo.di;


import com.example.demo.UIApp;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;


@Module
@InstallIn(SingletonComponent.class)
public interface AppProvides {

    @Provides
    static UIApp App() {
        return UIApp.getApp();
    }


}
