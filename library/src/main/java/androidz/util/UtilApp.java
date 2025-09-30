package androidz.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;


public final class UtilApp {
    private static Context applicationContext;
    private static PackageInfo packageInfo;
    private static boolean debuggable;

    public static void initialize(@NonNull Context context) {
        applicationContext = context.getApplicationContext();
        debuggable = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @NonNull
    public static Context getContext() {
        if (applicationContext == null)
            throw new IllegalStateException("UtilApp not initialized");
        return applicationContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static long getVersionCode() {
        PackageInfo pi = getPackageInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return pi.getLongVersionCode();
        }
        return pi.versionCode;
    }

    public static String getVersionName() {
        PackageInfo pi = getPackageInfo();
        return pi.versionName;
    }

    @NonNull
    public static PackageInfo getPackageInfo() {
        if (packageInfo != null) {
            return packageInfo;
        }
        try {
            packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return packageInfo;
    }
}