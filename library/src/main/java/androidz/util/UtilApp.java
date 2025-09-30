package androidz.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unused")
public final class UtilApp {
    private static Context appContext;
    private static boolean debuggable;
    private static PackageInfo packageInfo;

    public static void initialize(@NonNull Context context) {
        appContext = context.getApplicationContext();
        debuggable = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @NonNull
    public static Context getContext() {
        if (appContext == null)
            throw new IllegalStateException("UtilApp not initialized");
        return appContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    @NonNull
    public static CharSequence getAppName() {
        return getContext().getApplicationInfo().loadLabel(getContext().getPackageManager());
    }

    public static long getVersionCode() {
        PackageInfo pi = getPackageInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return pi.getLongVersionCode();
        }
        //noinspection deprecation
        return pi.versionCode;
    }

    @Nullable
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

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean runOnUiThread(@NonNull Runnable r) {
        return new Handler(Looper.getMainLooper()).post(r);
    }

    public static boolean isMainProcess() {
        String processName = getProcessName(getContext());
        ApplicationInfo info = getContext().getApplicationInfo();
        return TextUtils.equals(processName, info.processName);
    }

    @Nullable
    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    private static String getProcessName(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName();
        }

        // Try using ActivityThread to determine the current process name.
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread",
                    false, UtilApp.class.getClassLoader());
            final Object packageName;
            Method currentProcessName = activityThread.getDeclaredMethod("currentProcessName");
            currentProcessName.setAccessible(true);
            packageName = currentProcessName.invoke(null);
            if (packageName instanceof String) {
                return (String) packageName;
            }
        } catch (Throwable ignored) {
        }

        // Fallback to the most expensive way
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            if (processes != null && !processes.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.pid == pid) {
                        return process.processName;
                    }
                }
            }
        }
        return null;
    }
}