package androidz.util;

import android.os.SystemClock;
import android.view.View;

import com.ziojio.androidz.R;

import androidx.annotation.NonNull;

public class ClickUtil {

    public static void applyDebouncingClickListener(@NonNull View view, @NonNull View.OnClickListener listener) {
        view.setOnClickListener(v -> {
            if (isSingleClick(v)) {
                listener.onClick(v);
            }
        });
    }

    public static void applyDebouncingClickListener(@NonNull View view, int duration,
                                                    @NonNull View.OnClickListener listener) {
        view.setOnClickListener(v -> {
            if (isSingleClick(v, duration)) {
                listener.onClick(v);
            }
        });
    }

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
        if (nowClickTime - lastClickTime > duration) {
            view.setTag(R.id.view_tag_click_time, nowClickTime);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDoubleClick(@NonNull View view) {
        return isMultiClick(view, 800, 2);
    }

    public static boolean isMultiClick(@NonNull View view, int count) {
        return isMultiClick(view, 800, count);
    }

    public static boolean isMultiClick(@NonNull View view, int duration, int count) {
        Object click_time = view.getTag(R.id.view_tag_click_time);
        Object click_count = view.getTag(R.id.view_tag_click_count);
        long lastClickTime = click_time == null ? 0 : (long) click_time;
        int clickCount = click_count == null ? 0 : (int) click_count;
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
