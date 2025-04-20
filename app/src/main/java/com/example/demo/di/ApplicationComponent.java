package com.example.demo.di;

import com.example.demo.UIApp;
import com.example.demo.database.AppRoomDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ProvideModule.class)
public interface ApplicationComponent {
    UIApp app();

    AppRoomDatabase db();
}