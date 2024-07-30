package androidz;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public final class Androidz {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static boolean debuggable;
    private static Context appContext;

    public static void initialize(Context context) {
        appContext = context.getApplicationContext();
        debuggable = AppUtil.isDebuggable(appContext);
        ActivityStackManager.getInstance().register((Application) context);
    }

    public static Context getContext() {
        return appContext;
    }

    public static boolean isDebuggable() {
        return debuggable;
    }

    public static void setDebuggable(boolean debuggable) {
        Androidz.debuggable = debuggable;
    }

    public static Handler getMainHandler() {
        return handler;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
