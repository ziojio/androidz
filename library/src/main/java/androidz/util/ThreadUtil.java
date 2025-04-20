package androidz.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public final class ThreadUtil {

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static boolean isBackgroundThread() {
        return Looper.getMainLooper().getThread() != Thread.currentThread();
    }

    public static boolean runOnUiThread(@NonNull Runnable r) {
        return new Handler(Looper.getMainLooper()).post(r);
    }

}
