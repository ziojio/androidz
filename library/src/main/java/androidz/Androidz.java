package androidz;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class Androidz {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static boolean debuggable;

    private Androidz() {
    }

    public static void initialize(Context context) {
        App.attachApplication((Application) context.getApplicationContext());
        debuggable = App.INSTANCE.isDebuggable();
        if (isDebuggable()) {
            ActivityStackManager.INSTANCE.register((Application) context);
        }
    }

    public static Application getApp() {
        return App.INSTANCE;
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
