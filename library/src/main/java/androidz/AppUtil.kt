package androidz

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build

object AppUtil {

    @JvmOverloads
    @JvmStatic
    fun getPackageInfo(context: Context, flags: Int = 0): PackageInfo {
        val packageInfo: PackageInfo
        try {
            packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.PackageInfoFlags.of(flags.toLong())
                )
            } else {
                context.packageManager.getPackageInfo(context.packageName, flags)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException(e)
        }
        return packageInfo
    }

    @JvmStatic
    fun getAppName(context: Context): CharSequence {
        return context.applicationInfo.loadLabel(context.packageManager)
    }

    @JvmStatic
    fun getAppIcon(context: Context): Drawable {
        return context.applicationInfo.loadIcon(context.packageManager)
    }

    @JvmStatic
    fun isDebuggable(context: Context): Boolean {
        return (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }

    @JvmStatic
    fun getVersionName(context: Context): String {
        val pi = getPackageInfo(context)
        return if (pi.versionName == null) "" else pi.versionName
    }

    @JvmStatic
    fun getVersionCode(context: Context): Int {
        return getPackageInfo(context).versionCode
    }

    @JvmStatic
    fun isFirstTimeInstalled(context: Context): Boolean {
        val pi = getPackageInfo(context)
        return pi.firstInstallTime > 0 && pi.firstInstallTime == pi.lastUpdateTime
    }
}