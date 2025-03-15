package androidz.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;


public final class Androidz {
    private static final ActivityStackManager activityManager = new ActivityStackManager();
    private static Context appContext;
    private static boolean debuggable;

    public static void initialize(@NonNull Context context) {
        appContext = context.getApplicationContext();
        debuggable = (appContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        activityManager.register((Application) appContext);
    }

    @NonNull
    public static Context getContext() {
        if (appContext == null)
            throw new IllegalStateException("Androidz not initialize.");
        return appContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        Androidz.debuggable = debuggable;
    }

    public static ActivityStackManager getActivityManager() {
        return activityManager;
    }
}
