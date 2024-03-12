package androidz.util;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

public class Androidz {

    private static Application sApp;
    private static boolean sDebug;

    public static void initialize(@NonNull Context context) {
        if (sApp == null) {
            sApp = (Application) context.getApplicationContext();

            boolean debuggable = AppUtil.isDebuggable(sApp);
            setDebuggable(debuggable);

            if (debuggable) {
                ActivityStackManager.getInstance().register(sApp);
            }
        }
    }

    @NonNull
    public static Application getApp() {
        if (sApp != null) {
            return sApp;
        }
        throw new IllegalStateException("Androidz must be initialized");
    }

    public static boolean isDebuggable() {
        return sDebug;
    }

    public static void setDebuggable(boolean debug) {
        if (sDebug != debug) {
            sDebug = debug;
        }
    }
}
