package androidz.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import java.io.File;

import static androidz.util.FileUtil.delete;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     desc  : utils about clean
 * </pre>
 */
public final class CleanUtils {

    private CleanUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Clean the internal cache.
     * <p>directory: /data/data/package/cache</p>
     */
    public static boolean cleanInternalCache() {
        return delete(Androidz.getApp().getCacheDir());
    }

    /**
     * Clean the internal files.
     * <p>directory: /data/data/package/files</p>
     */
    public static boolean cleanInternalFiles() {
        return delete(Androidz.getApp().getFilesDir());
    }

    /**
     * Clean the internal databases.
     * <p>directory: /data/data/package/databases</p>
     */
    public static boolean cleanInternalDbs() {
        return delete(new File(Androidz.getApp().getFilesDir().getParent(), "databases"));
    }

    /**
     * Clean the internal database by name.
     * <p>directory: /data/data/package/databases/dbName</p>
     *
     * @param dbName The name of database.
     */
    public static boolean cleanInternalDbByName(final String dbName) {
        return Androidz.getApp().deleteDatabase(dbName);
    }

    /**
     * Clean the internal shared preferences.
     * <p>directory: /data/data/package/shared_prefs</p>
     */
    public static boolean cleanInternalSp() {
        return delete(new File(Androidz.getApp().getFilesDir().getParent(), "shared_prefs"));
    }

    /**
     * Clean the external cache.
     * <p>directory: /storage/emulated/0/android/data/package/cache</p>
     */
    public static boolean cleanExternalCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && delete(Androidz.getApp().getExternalCacheDir());
    }

    /**
     * Clean the custom directory.
     *
     * @param dirPath The path of directory.
     */
    public static boolean cleanCustomDir(final String dirPath) {
        if (dirPath == null || dirPath.isBlank()) return false;
        return delete(new File(dirPath));
    }

    public static void cleanAppUserData() {
        ActivityManager am = (ActivityManager) Androidz.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        am.clearApplicationUserData();
    }
}
