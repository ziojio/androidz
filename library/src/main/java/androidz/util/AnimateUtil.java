package androidz.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.annotation.NonNull;


public class AnimateUtil {

    /**
     * 圆形揭露动画显示 View
     */
    public static void circularRevealIn(@NonNull View view) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;
        float radius = (float) Math.hypot(cx, cy);
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, radius);
        anim.start();
        view.setVisibility(View.VISIBLE);
    }

    /**
     * 圆形揭露动画隐藏 View
     */
    public static void circularRevealOut(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;
            float radius = (float) Math.hypot(cx, cy);
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, radius, 0f);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
            anim.start();
        }
    }

    /**
     * 淡入动画
     */
    public static void fadeIn(@NonNull View view) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setListener(null).start();
    }

    /**
     * 淡出动画
     */
    public static void fadeOut(@NonNull View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            }).start();
        }
    }
}
