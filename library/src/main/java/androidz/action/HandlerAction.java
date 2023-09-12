package androidz.action;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public interface HandlerAction {

    Handler HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 获取 Handler
     */
    default Handler getHandler() {
        return HANDLER;
    }

    default boolean post(Runnable runnable) {
        return HANDLER.postAtTime(runnable, this, SystemClock.uptimeMillis());
    }

    /**
     * 延迟一段时间执行
     */
    default boolean postDelayed(Runnable runnable, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return HANDLER.postAtTime(runnable, this, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 移除单个消息回调
     */
    default void removeCallbacks(Runnable runnable) {
        HANDLER.removeCallbacks(runnable);
    }

    /**
     * 移除全部消息回调
     */
    default void removeCallbacks() {
        // 移除和当前对象相关的消息回调
        HANDLER.removeCallbacksAndMessages(this);
    }
}