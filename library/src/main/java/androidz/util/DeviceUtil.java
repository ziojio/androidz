package androidz.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;

public class DeviceUtil {

    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isAdbEnabled(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),
                Settings.Global.ADB_ENABLED, 0) > 0;
    }

    public static boolean isDevelopmentSettingsEnabled(Context context) {
        return Settings.Global.getInt(context.getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) > 0;
    }

}