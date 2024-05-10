package androidz

import android.os.SystemClock
import android.view.View


object ClickUtil {

    @JvmStatic
    @JvmOverloads
    fun applyDebouncing(view: View, duration: Int = 1000, listener: View.OnClickListener) {
        view.setOnClickListener { v: View ->
            if (isSingleClick(v, duration)) {
                listener.onClick(v)
            }
        }
    }

    /**
     * @param duration The duration of debouncing.
     */
    @JvmStatic
    @JvmOverloads
    fun isSingleClick(view: View, duration: Int = 1000): Boolean {
        val time = view.getTag(R.id.view_tag_click_time)
        val lastClickTime = if (time == null) 0 else time as Long
        val nowClickTime = SystemClock.uptimeMillis()
        if (nowClickTime - lastClickTime > duration) {
            view.setTag(R.id.view_tag_click_time, nowClickTime)
            return true
        } else {
            return false
        }
    }

    @JvmStatic
    @JvmOverloads
    fun isDoubleClick(view: View, duration: Long = 800): Boolean {
        return isMultiClick(view, 2, duration)
    }

    /**
     * @param duration The duration of interval.
     * @param count    The count of click.
     */
    @JvmStatic
    @JvmOverloads
    fun isMultiClick(view: View, count: Int, duration: Long = 800): Boolean {
        val click_time = view.getTag(R.id.view_tag_click_time)
        val click_count = view.getTag(R.id.view_tag_click_count)
        val lastClickTime = if (click_time == null) 0 else click_time as Long
        var clickCount = if (click_count == null) 0 else click_count as Int
        val nowClickTime = SystemClock.uptimeMillis()
        if (nowClickTime - lastClickTime < duration) {
            clickCount++
        } else {
            clickCount = 1
        }
        view.setTag(R.id.view_tag_click_time, nowClickTime)
        view.setTag(R.id.view_tag_click_count, clickCount)
        return clickCount == count
    }
}
