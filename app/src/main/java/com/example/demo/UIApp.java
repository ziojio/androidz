package com.example.demo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;

import com.example.demo.database.AppRoomDatabase;
import com.example.demo.database.TrackLog;
import com.example.demo.util.LogUtil;
import com.example.demo.util.Timber;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Date;

import androidx.annotation.NonNull;
import androidz.util.UtilApp;

public class UIApp extends Application {
    private static UIApp App;
    private AppRoomDatabase appDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("UIApp", "onCreate " + this);
        App = this;
        long start = SystemClock.elapsedRealtime();
        if (UtilApp.isDebuggable()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder();
            threadPolicyBuilder.detectNetwork();
            threadPolicyBuilder.detectCustomSlowCalls();
            threadPolicyBuilder.penaltyLog();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());

            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
            vmPolicyBuilder.detectActivityLeaks();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                vmPolicyBuilder.detectCleartextNetwork();
            }
            StrictMode.setVmPolicy(vmPolicyBuilder.build());

            Timber.plant(new LogUtil.FileLogTree(new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(new Date()))));
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NonNull String message, Throwable t) {
                    super.log(priority, tag, message, t);
                    LogUtil.saveLog(TrackLog.debug(tag, message));
                }
            });
        }

        MMKV.initialize(this);
        appDB = AppRoomDatabase.create(this);

        registerDefaultNetworkCallback();

        long time = SystemClock.elapsedRealtime() - start;
        Log.d("UIApp", "onCreate cost " + time + "ms");
    }

    public static UIApp getApp() {
        return App;
    }

    public static AppRoomDatabase getDB() {
        return App.appDB;
    }

    public static boolean isDebuggable() {
        return (App.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void registerDefaultNetworkCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ConnectivityManager c = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            c.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    Timber.d("onAvailable: network=" + network);
                }

                @Override
                public void onLosing(@NonNull Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    Timber.d("onLosing: network=" + network + ", maxMsToLive=" + maxMsToLive);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Timber.d("onLost: network=" + network);
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Timber.d("onUnavailable");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    Timber.d("onCapabilitiesChanged: network=" + network + ", networkCapabilities=" + networkCapabilities);
                }

                @Override
                public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties);
                    Timber.d("onLinkPropertiesChanged: linkProperties=" + linkProperties);
                }

                @Override
                public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
                    super.onBlockedStatusChanged(network, blocked);
                    Timber.d("onBlockedStatusChanged: blocked=" + blocked);
                }
            });
        }
    }
}

