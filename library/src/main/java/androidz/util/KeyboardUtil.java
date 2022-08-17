package androidz.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import static androidx.core.content.ContextCompat.getSystemService;


public final class KeyboardUtil {

    public static void showKeyboard(@NonNull View view) {
        showKeyboard(view, true);
    }

    public static void showKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.show(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void requestFocusAndShowKeyboard(@NonNull final View view) {
        requestFocusAndShowKeyboard(view, true);
    }

    public static void requestFocusAndShowKeyboard(@NonNull final View view, boolean useWindowInsetsController) {
        view.requestFocus();
        view.post(() -> showKeyboard(view, useWindowInsetsController));
    }

    public static void hideKeyboard(@NonNull View view) {
        hideKeyboard(view, true);
    }

    public static void hideKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.hide(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断软键盘是否可见
     * 默认软键盘最小高度为 200
     */
    public static boolean isKeyboardVisible(@NonNull Activity activity) {
        return isKeyboardVisible(activity, 200);
    }

    /**
     * 判断软键盘是否可见
     *
     * @param activity             activity
     * @param minHeightOfSoftInput 软键盘最小高度
     */
    public static boolean isKeyboardVisible(@NonNull Activity activity, int minHeightOfSoftInput) {
        return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput;
    }

    private static int getContentViewInvisibleHeight(@NonNull Activity activity) {
        final FrameLayout contentView = activity.findViewById(android.R.id.content);
        final View contentViewChild = contentView.getChildAt(0);
        final Rect outRect = new Rect();
        contentViewChild.getWindowVisibleDisplayFrame(outRect);
        return contentViewChild.getBottom() - outRect.bottom;
    }

    /**
     * 点击屏幕空白区域隐藏软键盘
     * <p>根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘</p>
     * <p>需重写 dispatchTouchEvent</p>
     *
     * @param focusView 聚焦的控件
     */
    public static void clickBlankArea2HideSoftInput(@NonNull MotionEvent ev, @NonNull View focusView) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isShouldHideKeyboard(focusView, ev)) {
                hideKeyboard(focusView);
            }
        }
    }

    /**
     * 根据 EditText 所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
     */
    private static boolean isShouldHideKeyboard(@NonNull View v, @NonNull MotionEvent event) {
        if (v instanceof EditText) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

}
