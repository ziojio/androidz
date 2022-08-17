package androidz.util;

import android.widget.Toast;

import java.lang.ref.SoftReference;

import androidz.Androidz;

public class ToastUtil {

    // 避免多个连续不停显示
    private static SoftReference<Toast> sToast;

    public static void showToast(CharSequence message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(CharSequence message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    public static void showToast(CharSequence message, int duration) {
        if (message == null) return;
        ThreadUtil.runOnUiThread(() -> {
            cancel();
            Toast toast = Toast.makeText(Androidz.getApp(), message, duration > 0 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            toast.show();
            sToast = new SoftReference<>(toast);
        });
    }

    public static void cancel() {
        if (sToast != null) {
            Toast toast = sToast.get();
            if (toast != null) {
                toast.cancel();
            }
        }
    }
}
