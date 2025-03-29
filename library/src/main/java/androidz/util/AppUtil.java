package androidz.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import java.io.File;


public final class AppUtil {

    @NonNull
    public static CharSequence getAppName() {
        Context context = Androidz.getContext();
        return context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    @NonNull
    public static String getVersionName() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.versionName == null ? "" : pi.versionName;
    }

    public static int getVersionCode() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.versionCode;
    }

    public static boolean isDebuggable() {
        ApplicationInfo info = Androidz.getContext().getApplicationInfo();
        return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static boolean isFirstInstall() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime;
    }

    @NonNull
    public static PackageInfo getPackageInfo(@NonNull Context context, int flags) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), flags);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * directory: /data/data/package/cache
     */
    public static boolean clearCache() {
        return FileUtil.deleteContents(Androidz.getContext().getCacheDir());
    }

    /**
     * directory: /data/data/package/files
     */
    public static boolean clearFiles() {
        return FileUtil.deleteContents(Androidz.getContext().getFilesDir());
    }

    /**
     * directory: /storage/emulated/0/android/data/package/cache
     */
    public static boolean clearExternalCache() {
        File file = Androidz.getContext().getExternalCacheDir();
        return file == null || FileUtil.deleteContents(file);
    }

    /**
     * directory: /storage/emulated/0/android/data/package/files
     */
    public static boolean clearExternalFiles() {
        File file = Androidz.getContext().getExternalFilesDir(null);
        return file == null || FileUtil.deleteContents(file);
    }

    /**
     * directory: /data/data/package/shared_prefs
     */
    public static boolean clearSharedPrefs() {
        return FileUtil.deleteContents(new File(Androidz.getContext().getFilesDir().getParentFile(), "shared_prefs"));
    }

    /**
     * directory: /data/data/package/databases/dbName
     */
    public static boolean deleteDatabase(@NonNull String dbName) {
        return Androidz.getContext().deleteDatabase(dbName);
    }

    /**
     * directory: /data/data/package/databases
     */
    public static boolean clearDatabase() {
        return FileUtil.deleteContents(new File(Androidz.getContext().getFilesDir().getParentFile(), "databases"));
    }

    public static boolean clearAppUserData() {
        ActivityManager am = (ActivityManager) Androidz.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        return am.clearApplicationUserData();
    }
}