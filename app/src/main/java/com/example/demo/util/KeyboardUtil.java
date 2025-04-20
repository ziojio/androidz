package com.example.demo.util;

import android.app.Activity;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

@SuppressWarnings("unused")
public final class KeyboardUtil {

    public static void showSoftKeyboard(@NonNull View view) {
        showSoftKeyboard(view, true);
    }

    public static void showSoftKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.show(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = ContextCompat.getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(@NonNull View view) {
        hideSoftKeyboard(view, true);
    }

    public static void hideSoftKeyboard(@NonNull View view, boolean useWindowInsetsController) {
        if (useWindowInsetsController) {
            WindowInsetsControllerCompat windowController = ViewCompat.getWindowInsetsController(view);
            if (windowController != null) {
                windowController.hide(WindowInsetsCompat.Type.ime());
                return;
            }
        }
        InputMethodManager imm = ContextCompat.getSystemService(view.getContext(), InputMethodManager.class);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 判断软键盘是否可见
     */
    public static boolean isSoftKeyboardVisible(@NonNull Activity activity) {
        View content = activity.findViewById(android.R.id.content);
        int height = content.getRootView().getHeight();
        Rect r = new Rect();
        content.getWindowVisibleDisplayFrame(r); // 获取应用内容显示区域
        int heightDiff = height - (r.bottom - r.top); // 未处理（显示状态栏时）状态栏的高度
        return heightDiff > height / 4;
    }

    /**
     * 点击输入框外隐藏软键盘，需重写 dispatchTouchEvent 监听所有的触摸事件
     */
    public static void hideSoftKeyboardByClick(@NonNull MotionEvent event, @NonNull View focusView) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!isClickView(focusView, event)) {
                hideSoftKeyboard(focusView);
            }
        }
    }

    private static boolean isClickView(@NonNull View view, @NonNull MotionEvent event) {
        int[] point = {0, 0};
        view.getLocationInWindow(point);
        int left = point[0], top = point[1], right = left + view.getWidth(), bottom = top + view.getHeight();
        float x = event.getX(), y = event.getY();
        return x > left && x < right && y > top && y < bottom;
    }

}
