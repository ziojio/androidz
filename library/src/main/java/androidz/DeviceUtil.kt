package androidz

import android.content.Context
import android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
import android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
import android.content.res.Resources
import android.os.Build
import android.provider.Settings.Global
import android.provider.Settings.Global.ADB_ENABLED
import android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
import android.provider.Settings.Secure
import java.util.Locale


object DeviceUtil {

    @JvmStatic
    val isTablet: Boolean
        get() {
            val size = Resources.getSystem().configuration.screenLayout and SCREENLAYOUT_SIZE_MASK
            return size >= SCREENLAYOUT_SIZE_LARGE
        }

    @JvmStatic
    fun isAdbEnabled(context: Context): Boolean {
        return Secure.getInt(context.contentResolver, ADB_ENABLED, 0) > 0
    }

    @JvmStatic
    fun isDevelopmentSettingsEnabled(context: Context): Boolean {
        return Global.getInt(context.contentResolver, DEVELOPMENT_SETTINGS_ENABLED, 0) > 0
    }

    @JvmStatic
    val isEmulator: Boolean
        get() = isEmulator(Build.DEVICE) || isEmulator(Build.MODEL) || isEmulator(Build.FINGERPRINT)


    private fun isEmulator(str: String): Boolean {
        val device = str.lowercase(Locale.US)
        return (device.contains("emulator")
                || device.contains("qemu")
                || device.contains("google_sdk")
                || device.contains("sdk_gphone")
                || device.contains("generic"))
    }
}