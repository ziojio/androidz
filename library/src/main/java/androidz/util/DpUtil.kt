package androidz.util

import android.content.Context


object DpUtil {

    @JvmStatic
    fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun dp2pxF(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale
    }

    @JvmStatic
    fun px2dp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dpF(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.density
        return px / scale
    }

    @JvmStatic
    fun px2sp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (px / scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2spF(context: Context, px: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return px / scale
    }

    @JvmStatic
    fun sp2px(context: Context, sp: Float): Int {
        val scale = context.resources.displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun sp2pxF(context: Context, sp: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return sp * scale
    }
}
