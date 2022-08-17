package androidz.util;

import android.app.ActivityManager;
import android.content.Context;

import java.io.File;

import androidx.annotation.NonNull;


public final class CleanUtil {

    /**
     * Clean the internal cache.
     * directory: /data/data/package/cache
     */
    public static boolean cleanInternalCache() {
        return FileUtil.deleteContents(Androidz.getContext().getCacheDir());
    }

    /**
     * Clean the internal files.
     * directory: /data/data/package/files
     */
    public static boolean cleanInternalFiles() {
        return FileUtil.deleteContents(Androidz.getContext().getFilesDir());
    }

    /**
     * Clean the external cache.
     * directory: /storage/emulated/0/android/data/package/cache
     */
    public static boolean cleanExternalCache() {
        File file = Androidz.getContext().getExternalCacheDir();
        return file == null || FileUtil.deleteContents(file);
    }

    /**
     * Clean the external file.
     * directory: /storage/emulated/0/android/data/package/file
     */
    public static boolean cleanExternalFile() {
        File file = Androidz.getContext().getExternalFilesDir(null);
        return file == null || FileUtil.deleteContents(file);
    }

    /**
     * Clean the internal shared preferences.
     * directory: /data/data/package/shared_prefs
     */
    public static boolean cleanInternalSp() {
        return FileUtil.deleteContents(new File(Androidz.getContext().getFilesDir().getParentFile(), "shared_prefs"));
    }

    /**
     * Clean the internal databases.
     * directory: /data/data/package/databases
     */
    public static boolean cleanInternalDbs() {
        return FileUtil.deleteContents(new File(Androidz.getContext().getFilesDir().getParentFile(), "databases"));
    }

    /**
     * Clean the internal database by name.
     * directory: /data/data/package/databases/dbName
     */
    public static boolean cleanInternalDb(@NonNull String dbName) {
        return Androidz.getContext().deleteDatabase(dbName);
    }

    public static boolean cleanAppUserData() {
        ActivityManager am = (ActivityManager) Androidz.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        return am.clearApplicationUserData();
    }
}