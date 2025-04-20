package com.example.demo.di;


import com.example.demo.UIApp;

import dagger.Module;
import dagger.Provides;

@Module
public interface AppProvides {

    @Provides
    static UIApp App() {
        return UIApp.getApp();
    }


}
