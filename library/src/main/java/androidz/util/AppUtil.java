package androidz.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;


public final class AppUtil {

    @NonNull
    public static PackageInfo getPackageInfo(@NonNull Context context, int flags) {
        PackageInfo packageInfo;
        try {
            String pn = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            packageInfo = pm.getPackageInfo(pn, flags);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return packageInfo;
    }

    @NonNull
    public static CharSequence getAppName() {
        Context context = Androidz.getContext();
        return context.getApplicationInfo().loadLabel(context.getPackageManager());
    }

    public static int getVersionCode() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.versionCode;
    }

    @NonNull
    public static String getVersionName() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.versionName == null ? "" : pi.versionName;
    }

    public static boolean isDebuggable() {
        return (Androidz.getContext().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    public static boolean isFirstInstall() {
        PackageInfo pi = getPackageInfo(Androidz.getContext(), 0);
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime;
    }
}