package com.example.demo.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import timber.log.Timber;

public class DebugLifecycleObserver implements DefaultLifecycleObserver {

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onCreate");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onStart");
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onResume");
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onPause");
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onStop");
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        Timber.tag(owner.getClass().getSimpleName()).d("onDestroy");
    }
}
