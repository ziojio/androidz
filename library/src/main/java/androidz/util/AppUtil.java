package androidz.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class AppUtil {

    @NonNull
    public static PackageInfo getPackageInfo(@NonNull Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return new PackageInfo();
    }

    public static String getAppName(@NonNull Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
    }

    public static Drawable getAppIcon(@NonNull Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    public static String getVersionName(@NonNull Context context) {
        PackageInfo pi = getPackageInfo(context);
        return pi.versionName == null ? "" : pi.versionName;
    }

    public static int getVersionCode(@NonNull Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static boolean isDebuggable(@NonNull Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static boolean isSystem(@NonNull Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }

    public static boolean isFirstTimeInstalled(@NonNull Context context) {
        PackageInfo pi = getPackageInfo(context);
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime;
    }

}