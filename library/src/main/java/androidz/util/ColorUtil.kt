package androidz.util

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils


object ColorUtil {

    @JvmStatic
    fun isDarkColor(@ColorInt color: Int): Boolean {
        return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) < 0.5
    }

    @JvmStatic
    fun isLightColor(@ColorInt color: Int): Boolean {
        return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) > 0.5
    }
}
