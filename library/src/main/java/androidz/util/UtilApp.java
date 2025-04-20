package androidz.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unused")
public final class UtilApp {
    private static final String TAG = "UtilApp";

    private static Application application;
    private static boolean debuggable;

    public static void initialize(@NonNull Context context) {
        Application application;
        if (context instanceof Application) {
            application = (Application) context;
        } else {
            application = (Application) context.getApplicationContext();
        }
        UtilApp.application = application;
        UtilApp.debuggable = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @NonNull
    public static Application getApp() {
        if (application == null)
            throw new IllegalStateException("UtilApp not initialized");
        return application;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    @NonNull
    public static CharSequence getAppName() {
        return getApp().getApplicationInfo().loadLabel(getApp().getPackageManager());
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
        try {
            return getApp().getPackageManager().getPackageInfo(getApp().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean runOnUiThread(@NonNull Runnable r) {
        return new Handler(Looper.getMainLooper()).post(r);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Network network = manager.getActiveNetwork();
                if (network != null) {
                    NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
                    if (networkCapabilities != null) {
                        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                    }
                }
            } else {
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    //noinspection deprecation
                    return networkInfo.isConnected();
                }
            }
        }
        return false;
    }

    public static boolean isMainProcess() {
        String processName = getProcessName(getApp());
        ApplicationInfo info = getApp().getApplicationInfo();
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
            Log.d(TAG, "Unable to check ActivityThread for processName", exception);
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