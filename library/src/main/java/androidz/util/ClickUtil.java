package androidz.util;

import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;


public final class ClickUtil {

    public static boolean isSingleClick(@NonNull View view) {
        return isSingleClick(view, 1000);
    }

    /**
     * @param duration The duration of debouncing.
     */
    public static boolean isSingleClick(@NonNull View view, int duration) {
        Object time = view.getTag(R.id.view_tag_click_time);
        long lastClickTime = time == null ? 0 : (long) time;
        long nowClickTime = SystemClock.uptimeMillis();
        view.setTag(R.id.view_tag_click_time, nowClickTime);
        return nowClickTime - lastClickTime > duration;
    }

    public static boolean isDoubleClick(@NonNull View view) {
        return isMultiClick(view, 2, 800);
    }

    /**
     * @param count    The count of click.
     * @param duration The duration of interval.
     */
    public static boolean isMultiClick(@NonNull View view, int count, int duration) {
        Object time = view.getTag(R.id.view_tag_click_time);
        long lastClickTime = time == null ? 0 : (long) time;
        Object click = view.getTag(R.id.view_tag_click_count);
        long clickCount = click == null ? 0 : (long) click;
        long nowClickTime = SystemClock.uptimeMillis();
        if (nowClickTime - lastClickTime < duration) {
            clickCount++;
        } else {
            clickCount = 1;
        }
        view.setTag(R.id.view_tag_click_time, nowClickTime);
        view.setTag(R.id.view_tag_click_count, clickCount);
        return clickCount == count;
    }
}
