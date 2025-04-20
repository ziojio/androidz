package androidz.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;


public final class LoadingDialog {

    @SuppressLint("StaticFieldLeak")
    private static InternalLoadingDialog dialog;

    public static void showLoading(@NonNull Activity activity) {
        showLoading(activity, new Options());
    }

    public static void showLoading(@NonNull Activity activity, @NonNull Options options) {
        hide();
        dialog = new InternalLoadingDialog(activity, options);
        // 主动取消情况下，清除引用
        dialog.setOnDismissListener(d -> dialog = null);
        dialog.show();
    }

    public static void hide() {
        if (dialog != null) {
            dialog.setOnDismissListener(null);
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 在显示期间禁止屏幕旋转，关闭后恢复
     */
    public static final class InternalLoadingDialog extends AppCompatDialog {
        private final Activity activity;
        private final Options options;
        private int orientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;

        public InternalLoadingDialog(@NonNull Activity activity, @NonNull Options options) {
            super(activity);
            this.activity = activity;
            this.options = options;
            setCancelable(options.cancelable);
            setCanceledOnTouchOutside(options.canceledOnTouchOutside);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(options.loading != 0 ? options.loading : R.layout.loading_dialog);
            if (options.message != 0) {
                TextView textView = findViewById(R.id.loading_message);
                if (textView != null) {
                    textView.setText(options.message);
                }
            }
            if (options.icon != 0) {
                ProgressBar progressBar = findViewById(R.id.loading_progress_bar);
                if (progressBar != null) {
                    progressBar.setIndeterminateDrawable(ContextCompat.getDrawable(activity, options.icon));
                    progressBar.setIndeterminateTintList(null);
                }
            }
        }

        @Override
        public void show() {
            orientation = activity.getRequestedOrientation();
            if (orientation != ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
            }
            super.show();
        }

        @Override
        public void dismiss() {
            if (orientation != ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(orientation);
                orientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;
            }
            super.dismiss();
        }
    }

    public static class Options {
        /**
         * 自定义布局
         */
        @LayoutRes
        public int loading;
        /**
         * 提示消息
         */
        @StringRes
        public int message;
        /**
         * 提示图标：IndeterminateDrawable
         */
        @DrawableRes
        public int icon;
        public boolean cancelable = true;
        public boolean canceledOnTouchOutside = false;
    }
}
