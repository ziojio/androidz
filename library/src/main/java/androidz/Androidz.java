package androidz;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import androidz.util.AppUtil;

public class Androidz {

    private static Application sApp;
    private static boolean sDebug;

    public static void initialize(@NonNull Context context) {
        if (sApp == null) {
            Application application = (Application) context.getApplicationContext();
            sApp = application;
            ActivityStackManager.getInstance().register(application);
            setDebug(AppUtil.isDebuggable(application));
        }
    }

    public static Application getApp() {
        if (sApp != null) {
            return sApp;
        }
        throw new IllegalStateException("Androidz must be initialized");
    }

    public static boolean isDebug() {
        return sDebug;
    }

    public static void setDebug(boolean debug) {
        if (sDebug != debug) {
            sDebug = debug;
        }
    }
}
