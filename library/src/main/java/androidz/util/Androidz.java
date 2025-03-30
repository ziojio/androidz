package androidz.util;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import androidx.annotation.NonNull;


public final class Androidz {
    private static Context appContext;
    private static boolean debuggable;
    private static final ActivityLifecycleManager activityManager = new ActivityLifecycleManager();

    public static void initialize(@NonNull Context context) {
        Application application = (Application) context.getApplicationContext();
        appContext = application;
        debuggable = (application.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        application.registerActivityLifecycleCallbacks(activityManager);
    }

    @NonNull
    public static Context getContext() {
        if (appContext == null)
            throw new IllegalStateException("Androidz not initialize");
        return appContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        Androidz.debuggable = debuggable;
    }

    public static ActivityLifecycleManager getActivityManager() {
        return activityManager;
    }
}
