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
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;


public final class UtilApp {
    private static Application application;
    private static PackageInfo packageInfo;
    private static final ActivityLifecycleManager activityManager = new ActivityLifecycleManager();
    private static boolean loggable;

    @MainThread
    public static void initialize(@NonNull Context context) {
        if (application == null) {
            application = (Application) context.getApplicationContext();
            application.registerActivityLifecycleCallbacks(activityManager);
            loggable = (application.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        }
    }

    public static boolean isLoggable() {
        return loggable;
    }

    public static void setLoggable(boolean loggable) {
        UtilApp.loggable = loggable;
    }

    @NonNull
    public static Context getApp() {
        if (application == null)
            throw new IllegalStateException("Androidz not initialize");
        return application;
    }

    @NonNull
    public static ActivityLifecycleManager getActivityManager() {
        return activityManager;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean isDebuggable() {
        Context context = getApp();
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static CharSequence getAppName() {
        Context context = getApp();
        return context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    @NonNull
    public static String getVersionName() {
        PackageInfo pi = getPackageInfo();
        return pi.versionName == null ? "" : pi.versionName;
    }

    public static int getVersionCode() {
        PackageInfo pi = getPackageInfo();
        return pi.versionCode;
    }

    public static boolean isFirstInstall() {
        PackageInfo pi = getPackageInfo();
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime;
    }

    @NonNull
    public static PackageInfo getPackageInfo() {
        if (packageInfo != null) {
            return packageInfo;
        }
        synchronized (UtilApp.class) {
            if (packageInfo != null) {
                return packageInfo;
            }
            try {
                Context context = getApp();
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                packageInfo.applicationInfo = context.getApplicationInfo();
                return packageInfo;
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean clearFiles() {
        return FileUtil.deleteContents(getApp().getFilesDir());
    }

    public static boolean clearCache() {
        return FileUtil.deleteContents(getApp().getCacheDir());
    }

    public static boolean clearExternalFiles() {
        File file = getApp().getExternalFilesDir(null);
        return file == null || FileUtil.deleteContents(file);
    }

    public static boolean clearExternalCache() {
        File file = getApp().getExternalCacheDir();
        return file == null || FileUtil.deleteContents(file);
    }

    public static boolean clearSharedPrefs() {
        File file = new File(getApp().getFilesDir().getParentFile(), "shared_prefs");
        return FileUtil.deleteContents(file);
    }

    public static boolean clearDatabases() {
        File file = new File(getApp().getFilesDir().getParentFile(), "databases");
        return FileUtil.deleteContents(file);
    }

    public static boolean clearAppUserData() {
        ActivityManager am = (ActivityManager) getApp().getSystemService(Context.ACTIVITY_SERVICE);
        return am.clearApplicationUserData();
    }

    public static boolean isMainProcess() {
        Context context = getApp();
        ApplicationInfo info = context.getApplicationInfo();
        String processName = getProcessName(context);
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
        } catch (Throwable exception) {
            Log.d(UtilApp.class.getSimpleName(), "Unable to check ActivityThread for processName", exception);
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
