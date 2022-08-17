package androidz.util;

import android.os.SystemClock;
import android.view.View;

import com.ziojio.androidz.R;

public class ClickUtil {

    public static boolean isSingleClick(View view) {
        return isSingleClick(view, 1000);
    }

    public static boolean isSingleClick(View view, int interval) {
        Object time = view.getTag(R.id.view_tag_click_time);
        long lastClickTime = time == null ? 0 : (long) time;
        long nowClickTime = SystemClock.uptimeMillis();
        if (nowClickTime - lastClickTime > interval) {
            view.setTag(R.id.view_tag_click_time, nowClickTime);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDoubleClick(View view) {
        return isMultiClick(view, 800, 2);
    }

    public static boolean isMultiClick(View view, int interval, int count) {
        Object click_time = view.getTag(R.id.view_tag_click_time);
        Object click_count = view.getTag(R.id.view_tag_click_count);
        long lastClickTime = click_time == null ? 0 : (long) click_time;
        int clickCount = click_count == null ? 0 : (int) click_count;
        long nowClickTime = SystemClock.uptimeMillis();
        if (nowClickTime - lastClickTime < interval) {
            clickCount++;
        } else {
            clickCount = 1;
        }
        view.setTag(R.id.view_tag_click_time, nowClickTime);
        view.setTag(R.id.view_tag_click_count, clickCount);
        return clickCount == count;
    }

}
