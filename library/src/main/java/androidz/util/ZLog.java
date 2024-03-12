package androidz.util;

import static androidz.util.Androidz.isDebuggable;

import android.util.Log;

public class ZLog {

    public static void v(String tag, String msg) {
        if (isDebuggable()) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebuggable()) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebuggable()) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebuggable()) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (isDebuggable()) {
            Log.w(tag, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.w(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebuggable()) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, Throwable tr) {
        if (isDebuggable()) {
            Log.e(tag, "", tr);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.e(tag, msg, tr);
        }
    }

    public static void wtf(String tag, String msg) {
        if (isDebuggable()) {
            Log.wtf(tag, msg);
        }
    }

    public static void wtf(String tag, Throwable tr) {
        if (isDebuggable()) {
            Log.wtf(tag, tr);
        }
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        if (isDebuggable()) {
            Log.wtf(tag, msg, tr);
        }
    }
}
