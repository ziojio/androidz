package androidz.util;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.NonNull;

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
            if (metrics.density == 0f) return 0f;
            else return pixelValue / metrics.density;
        }
    }

    public static float px2sp(@NonNull Context context, float pixelValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            return TypedValue.deriveDimension(TypedValue.COMPLEX_UNIT_SP, pixelValue, metrics);
        } else {
            if (metrics.scaledDensity == 0f) return 0f;
            else return pixelValue / metrics.scaledDensity;
        }
    }

}
