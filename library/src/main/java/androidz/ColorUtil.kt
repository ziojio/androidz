package androidz

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils

object ColorUtil {

    fun isDarkColor(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) luminance: Float = 0.5f
    ): Boolean {
        return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) < luminance
    }

    fun isLightColor(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) luminance: Float = 0.5f
    ): Boolean {
        return color != Color.TRANSPARENT && ColorUtils.calculateLuminance(color) > luminance
    }
}
