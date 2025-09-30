package com.example.demo;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.demo.database.AppRoomDatabase;
import com.example.demo.util.CrashHandler;
import com.example.demo.util.LogUtil;
import com.example.demo.util.Timber;
import com.tencent.mmkv.MMKV;


public class UIApp extends Application {
    private static UIApp INSTANCE;
    private static AppRoomDatabase appDB;
    private static long versionCode;
    private static String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        Log.d("UIApp", "onCreate " + this);
        long start = SystemClock.elapsedRealtime();
        CrashHandler.register(this);
        MMKV.initialize(this);
        appDB = AppRoomDatabase.create(this);

        if (isDebuggable()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder();
            threadPolicyBuilder.detectNetwork();
            threadPolicyBuilder.detectCustomSlowCalls();
            threadPolicyBuilder.penaltyLog();
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());

            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder();
            vmPolicyBuilder.detectActivityLeaks();
            vmPolicyBuilder.detectCleartextNetwork();
            StrictMode.setVmPolicy(vmPolicyBuilder.build());

            Timber.plant(new Timber.DebugTree());
            Timber.plant(new LogUtil.FileLogTree(LogUtil.getLogFile(this)));
        }

        registerDefaultNetworkCallback();

        long time = SystemClock.elapsedRealtime() - start;
        Log.d("UIApp", "onCreate " + time + "ms");
    }

    public static UIApp getApp() {
        return INSTANCE;
    }

    public static AppRoomDatabase getDB() {
        return appDB;
    }

    public static long getVersionCode() {
        if (versionCode != 0) {
            return versionCode;
        }
        try {
            PackageInfo packageInfo = INSTANCE.getPackageManager().getPackageInfo(INSTANCE.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode();
            } else {
                versionCode = packageInfo.versionCode;
            }
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("UIApp", "getPackageInfo", e);
        }
        return versionCode;
    }

    public static String getVersionName() {
        if (versionName != null) {
            return versionName;
        }
        try {
            PackageInfo packageInfo = INSTANCE.getPackageManager().getPackageInfo(INSTANCE.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                versionCode = packageInfo.getLongVersionCode();
            } else {
                versionCode = packageInfo.versionCode;
            }
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("UIApp", "getPackageInfo", e);
        }
        return versionName;
    }

    public static boolean isDebuggable() {
        return (INSTANCE.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    private void registerDefaultNetworkCallback() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
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
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(callback);
        } else {
            manager.registerNetworkCallback(new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build(), callback);
        }
    }
}

