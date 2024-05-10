package androidz

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils
import kotlin.math.hypot


object AnimateUtil {

    /**
     * 圆形揭露动画显示 View
     */
    @JvmOverloads
    @JvmStatic
    fun circularRevealIn(view: View, duration: Long = 0) {
        if (view.visibility != View.VISIBLE) {
            val cx = view.width / 2
            val cy = view.height / 2
            val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, radius)
            if (duration > 0) {
                anim.duration = duration
            }
            anim.start()
            view.visibility = View.VISIBLE
        }
    }

    /**
     * 圆形揭露动画隐藏 View
     */
    @JvmOverloads
    @JvmStatic
    fun circularRevealOut(view: View, duration: Long = 0, visibility: Int = View.GONE) {
        if (view.visibility == View.VISIBLE) {
            val cx = view.width / 2
            val cy = view.height / 2
            val radius = hypot(cx.toDouble(), cy.toDouble()).toFloat()
            val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0f)
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = visibility
                }
            })
            if (duration > 0) {
                anim.duration = duration
            }
            anim.start()
        }
    }

    /**
     * 淡入动画
     */
    @JvmOverloads
    @JvmStatic
    fun fadeIn(view: View, duration: Long = 0) {
        if (view.visibility != View.VISIBLE) {
            view.alpha = 0f
            view.visibility = View.VISIBLE
            val anim = view.animate().alpha(1f).setListener(null)
            if (duration > 0) {
                anim.duration = duration
            }
            anim.start()
        }
    }

    /**
     * 淡出动画
     */
    @JvmOverloads
    @JvmStatic
    fun fadeOut(view: View, duration: Long = 0, visibility: Int = View.GONE) {
        if (view.visibility == View.VISIBLE) {
            val anim = view.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    view.visibility = visibility
                }
            })
            if (duration > 0) {
                anim.duration = duration
            }
            anim.start()
        }
    }
}
