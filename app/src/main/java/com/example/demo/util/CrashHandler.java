package com.example.demo.util;

import android.app.Application;
import android.os.Build;

import com.example.demo.UIApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Crash to file
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static void register(Application application) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(application));
        FileUtil.deleteOlderFiles(application.getCacheDir(), 100, 0);
    }

    private final Application mApplication;
    private final Thread.UncaughtExceptionHandler mNextHandler;

    private CrashHandler(Application application) {
        mApplication = application;
        mNextHandler = Thread.getDefaultUncaughtExceptionHandler();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        File file = new File(mApplication.getCacheDir(), "crash_" + System.currentTimeMillis() + ".txt");
        try (PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            printWriter.println("brand: " + Build.BRAND);
            printWriter.println("model: " + Build.MODEL);
            printWriter.println("release: " + Build.VERSION.RELEASE);
            printWriter.println("versionCode: " + UIApp.getVersionCode());
            printWriter.println("versionName: " + UIApp.getVersionName());
            e.printStackTrace(printWriter);
        } catch (Throwable ignored) {
        }
        if (mNextHandler != null) {
            mNextHandler.uncaughtException(t, e);
        }
    }
}