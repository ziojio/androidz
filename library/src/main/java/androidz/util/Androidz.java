package androidz.util;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;


public final class Androidz {
    static Context appContext;
    static boolean debuggable;

    public static void initialize(@NonNull Context context) {
        appContext = context.getApplicationContext();
        debuggable = AppUtil.isDebuggable();
        ActivityStackManager.getInstance().register((Application) context);
    }

    @NonNull
    public static Context getContext() {
        if (appContext == null)
            throw new IllegalStateException("not initialize");
        return appContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        Androidz.debuggable = debuggable;
    }
}
