package androidz.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class FocusUtil {

    public static void requestFocus(@NonNull View view) {
        view.post(view::requestFocus);
    }

    public static void clearFocus(@NonNull View view) {
        // 只有把焦点落在其他控件上，输入框本身的焦点才会真正失去
        view.post(() -> {
            focusParent(view);
            view.clearFocus();
            KeyboardUtil.hideKeyboard(view);
        });
    }

    public static void clearFocus(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isActive()) {
                    clearFocus(activity.getCurrentFocus());
                }
            }
        }
    }

    private static void focusParent(View view) {
        if (view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                parent.setDefaultFocusHighlightEnabled(false);
            }
            parent.setFocusable(true);
            parent.setFocusableInTouchMode(true);
            parent.requestFocus();
        }
    }
}