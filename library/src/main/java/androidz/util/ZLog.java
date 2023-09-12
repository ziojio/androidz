package androidz.util;

import android.util.Log;

import static androidz.util.Androidz.isDebuggable;

public class ZLog {

    public static void v(String tag, String msg) {
        if (isDebuggable()) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebuggable()) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebuggable()) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebuggable()) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Throwable e) {
        if (isDebuggable()) {
            Log.w(tag, e);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (isDebuggable()) {
            Log.w(tag, msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebuggable()) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable e) {
        if (isDebuggable()) {
            Log.e(tag, msg, e);
        }
    }

    public static void wtf(String tag, String msg) {
        if (isDebuggable()) {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String tag, Throwable e) {
        if (isDebuggable()) {
            Log.wtf(tag, e);
        }
    }

    public static void wtf(String tag, String msg, Throwable e) {
        if (isDebuggable()) {
            Log.wtf(tag, msg, e);
        }
    }
}
