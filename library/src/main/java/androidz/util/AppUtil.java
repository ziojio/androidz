package androidz.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;


public class AppUtil {

    @NonNull
    public static PackageInfo getPackageInfo(@NonNull Context context, int flags) throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.PackageInfoFlags.of(flags));
        } else {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), flags);
        }
        return packageInfo;
    }

    @NonNull
    public static PackageInfo getPackageInfo(@NonNull Context context) {
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageInfo(context, 0);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return packageInfo;
    }

    public static CharSequence getAppName(@NonNull Context context) {
        return context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    public static Drawable getAppIcon(@NonNull Context context) {
        return context.getApplicationInfo().loadIcon(context.getPackageManager());
    }

    public static boolean isDebuggable(@NonNull Context context) {
        return (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @NonNull
    public static String getVersionName(@NonNull Context context) {
        PackageInfo pi = getPackageInfo(context);
        return pi.versionName == null ? "" : pi.versionName;
    }

    public static int getVersionCode(@NonNull Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static boolean isFirstTimeInstalled(@NonNull Context context) {
        PackageInfo pi = getPackageInfo(context);
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime;
    }

}