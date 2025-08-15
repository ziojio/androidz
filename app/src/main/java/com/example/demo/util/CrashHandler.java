package com.example.demo.util;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;

/**
 * Crash to file
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static void register(Application application) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(application));
        // TODO delete old files
    }

    private final Application mApplication;
    private final Thread.UncaughtExceptionHandler mNextHandler;

    private CrashHandler(Application application) {
        mApplication = application;
        mNextHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        File cacheDir = mApplication.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = mApplication.getCacheDir();
        }
        File file = new File(cacheDir, "crash_" + System.currentTimeMillis() + ".txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(t.toString());
            fileWriter.write(Log.getStackTraceString(e));
        } catch (Throwable ignored) {
        }
        if (mNextHandler != null) {
            mNextHandler.uncaughtException(t, e);
        }
    }
}