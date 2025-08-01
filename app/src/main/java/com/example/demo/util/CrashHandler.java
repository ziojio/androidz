package com.example.demo.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;

/**
 * Crash处理
 */
public final class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String CRASH_FILE_NAME = "crash_file";
    private static final String KEY_CRASH_TIME = "key_crash_time";

    public static void register(Application application) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(application));
    }

    private final Application mApplication;
    private final Thread.UncaughtExceptionHandler mNextHandler;

    private CrashHandler(Application application) {
        mApplication = application;
        mNextHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (mNextHandler != null && getClass().getName().equals(mNextHandler.getClass().getName())) {
            // 不能重复注册 Crash 监听
            throw new IllegalStateException("CrashHandler cannot be registered repeatedly");
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        SharedPreferences sharedPreferences = mApplication.getSharedPreferences(CRASH_FILE_NAME, Context.MODE_PRIVATE);
        long currentCrashTime = System.currentTimeMillis();
        long lastCrashTime = sharedPreferences.getLong(KEY_CRASH_TIME, 0);
        // 记录当前崩溃的时间，以便下次崩溃时进行比对
        sharedPreferences.edit().putLong(KEY_CRASH_TIME, currentCrashTime).commit();

        // 致命异常标记：如果上次崩溃的时间距离当前崩溃小于 1 分钟，那么判定为致命异常
        boolean deadlyCrash = currentCrashTime - lastCrashTime < 1000 * 60;
        if (!deadlyCrash) {
            // 如果不是致命的异常就自动重启应用
            RestartActivity.start(mApplication);
        }

        // 不去触发系统的崩溃处理（com.android.internal.os.RuntimeInit$KillApplicationHandler）
        if (mNextHandler != null && !mNextHandler.getClass().getName().startsWith("com.android.internal.os")) {
            mNextHandler.uncaughtException(t, e);
        }

        // 杀死进程（这个事应该是系统干的，但是它会多弹出一个崩溃对话框，所以需要我们自己手动杀死进程）
        Process.killProcess(Process.myPid());
        System.exit(10);
    }
}