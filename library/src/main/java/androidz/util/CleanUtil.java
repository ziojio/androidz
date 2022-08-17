package androidz.util;

import android.app.ActivityManager;
import android.content.Context;

import java.io.File;


public class CleanUtil {

    private CleanUtil() {
    }

    /**
     * Clean the internal cache.
     * <p>directory: /data/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalCache() {
        return FileUtil.deleteContents(Androidz.getApp().getCacheDir());
    }

    /**
     * Clean the internal files.
     * <p>directory: /data/data/package/files</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalFiles() {
        return FileUtil.deleteContents(Androidz.getApp().getFilesDir());
    }

    /**
     * Clean the internal shared preferences.
     * <p>directory: /data/data/package/shared_prefs</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalSp() {
        return FileUtil.deleteContents(new File(Androidz.getApp().getFilesDir().getParent(), "shared_prefs"));
    }

    /**
     * Clean the internal databases.
     * <p>directory: /data/data/package/databases</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDbs() {
        return FileUtil.deleteContents(new File(Androidz.getApp().getFilesDir().getParent(), "databases"));
    }

    /**
     * Clean the internal database by name.
     * <p>directory: /data/data/package/databases/dbName</p>
     *
     * @param dbName The name of database.
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanInternalDb(final String dbName) {
        return Androidz.getApp().deleteDatabase(dbName);
    }

    /**
     * Clean the external file.
     * <p>directory: /storage/emulated/0/android/data/package/file</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanExternalFile() {
        File file = Androidz.getApp().getExternalFilesDir(null);
        return file == null || FileUtil.deleteContents(file);
    }

    /**
     * Clean the external cache.
     * <p>directory: /storage/emulated/0/android/data/package/cache</p>
     *
     * @return {@code true}: success<br>{@code false}: fail
     */
    public static boolean cleanExternalCache() {
        File file = Androidz.getApp().getExternalCacheDir();
        return file == null || FileUtil.deleteContents(file);
    }

    public static void cleanAppUserData() {
        ActivityManager am = (ActivityManager) Androidz.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        am.clearApplicationUserData();
    }

}