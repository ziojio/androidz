package androidz.util;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.lang.ref.SoftReference;

public class ToastUtil {

    // 避免多个连续不停显示
    private static SoftReference<Toast> sToast;

    public static void showToast(@NonNull CharSequence message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    public static void showToastLong(@NonNull CharSequence message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    public static void showToast(@NonNull CharSequence message, int duration) {
        ThreadUtil.runOnUiThread(() -> {
            cancel();
            Context context = ActivityStackManager.getInstance().getTopActivity();
            if (context == null) {
                context = Androidz.getApp();
            }
            int durationFlag = duration > 0 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, message, durationFlag);
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
