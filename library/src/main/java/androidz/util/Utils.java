package androidz.util;

import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Utils
 */
@SuppressWarnings({"unused"})
public final class Utils {

    public static float dp2px(@NonNull Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics);
    }

    public static float sp2px(@NonNull Context context, float value) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, metrics);
    }

    public static float px2dp(@NonNull Context context, float pixelValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_DIP, pixelValue, metrics);
        } else {
            if (metrics.density == 0) {
                return 0f;
            } else {
                return pixelValue / metrics.density;
            }
        }
    }

    @SuppressWarnings({"deprecation"})
    public static float px2sp(@NonNull Context context, float pixelValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_SP, pixelValue, metrics);
        } else {
            if (metrics.scaledDensity == 0) {
                return 0;
            } else {
                return pixelValue / metrics.scaledDensity;
            }
        }
    }

    public static boolean isSingleClick(@NonNull View view) {
        return isSingleClick(view, 1000);
    }

    /**
     * @param duration millisecond
     */
    public static boolean isSingleClick(@NonNull View view, int duration) {
        Object time = view.getTag(R.id.view_tag_click_time);
        long lastClickTime = time == null ? 0 : (long) time;
        long nowClickTime = SystemClock.uptimeMillis();
        view.setTag(R.id.view_tag_click_time, nowClickTime);
        return nowClickTime - lastClickTime > duration;
    }
}
