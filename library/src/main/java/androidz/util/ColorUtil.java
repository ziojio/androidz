package androidz.util;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

public class ColorUtil {

    public static boolean isDarkColor(@ColorInt int color) {
        return color != TRANSPARENT && ColorUtils.calculateLuminance(color) < 0.5;
    }

    public static boolean isLightColor(@ColorInt int color) {
        return color != TRANSPARENT && ColorUtils.calculateLuminance(color) > 0.5;
    }

}
