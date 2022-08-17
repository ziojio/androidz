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

import com.example.demo.database.room.AppDB;
import com.example.demo.database.room.entity.TrackLog;
import com.example.demo.log.FileLogTree;
import com.example.demo.log.LogUtil;
import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.Date;

import androidx.annotation.NonNull;
import androidz.util.AppUtil;
import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class UIApp extends Application {
    private static UIApp App;
    private AppDB appDB;

    @Override
    public void onCreate() {
        super.onCreate();
        App = this;
        long start = SystemClock.elapsedRealtime();
        if (AppUtil.isDebuggable()) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectNetwork()
                    .detectDiskWrites()
                    .detectCustomSlowCalls()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog().build());

            Timber.plant(new FileLogTree(new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(new Date()))));
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, @NonNull String message, Throwable t) {
                    LogUtil.saveLog(new TrackLog(tag, message));
                    super.log(priority, tag, message, t);
                }
            });
        }
        Timber.d("onCreate " + this);

        MMKV.initialize(this);
        appDB = AppDB.create(this);
        long time = SystemClock.elapsedRealtime() - start;

        registerDefaultNetworkCallback();

        Timber.d("onCreate cost time " + time + "ms");
    }

    public static UIApp getApp() {
        return App;
    }

    public static AppDB getDB() {
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
                    Timber.d("onLosing: maxMsToLive=" + maxMsToLive);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Timber.d("onLost: network=" + network);
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Timber.d("onUnavailable: ");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    Timber.d("onCapabilitiesChanged: network=" + network + " networkCapabilities=" + networkCapabilities);
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

