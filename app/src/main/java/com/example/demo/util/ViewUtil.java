package com.example.demo.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

@SuppressWarnings("unused")
public final class ViewUtil {

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

    @SuppressWarnings("deprecation")
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
        Object time = view.getTag(Integer.MIN_VALUE);
        long lastClickTime = time == null ? 0 : (long) time;
        long nowClickTime = SystemClock.uptimeMillis();
        view.setTag(Integer.MIN_VALUE);
        return nowClickTime - lastClickTime > duration;
    }

    public static void requestFocus(@NonNull View view) {
        view.post(view::requestFocus);
    }

    public static void clearFocus(@NonNull View view) {
        // 只有把焦点落在其他控件上，输入框本身的焦点才会真正失去
        view.post(() -> {
            focusParent(view);
            view.clearFocus();
            KeyboardUtil.hideSoftKeyboard(view);
        });
    }

    public static void clearFocus(@NonNull Activity activity) {
        View currentFocus = activity.getCurrentFocus();
        if (currentFocus != null && currentFocus.getWindowToken() != null) {
            InputMethodManager imm = ContextCompat.getSystemService(activity, InputMethodManager.class);
            if (imm != null && imm.isActive()) {
                clearFocus(currentFocus);
            }
        }
    }

    private static void focusParent(@NonNull View view) {
        if (view.getParent() instanceof ViewGroup parent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                parent.setDefaultFocusHighlightEnabled(false);
            }
            parent.setFocusable(true);
            parent.setFocusableInTouchMode(true);
            parent.requestFocus();
        }
    }
}