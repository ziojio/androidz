package androidz.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

public final class LoadingDialog {
    private static Dialog dialog;

    public static void show(@NonNull Activity activity) {
        show(activity, new Options());
    }

    public static void show(@NonNull Activity activity, @NonNull Options options) {
        hide();
        dialog = new InternalLoadingDialog(activity, options);
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
    private static final class InternalLoadingDialog extends AppCompatDialog implements DialogInterface.OnDismissListener {
        private final Activity activity;
        private final Options options;
        private int orientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;

        public InternalLoadingDialog(@NonNull Activity activity, @NonNull Options options) {
            super(activity, options.theme);
            this.activity = activity;
            this.options = options;
            setCancelable(options.cancelable);
            setCanceledOnTouchOutside(options.canceledOnTouchOutside);
            setOnDismissListener(this);
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
            super.dismiss();
            if (orientation != ActivityInfo.SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(orientation);
                orientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;
            }
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            LoadingDialog.dialog = null;
        }
    }

    public static class Options {
        /**
         * 自定义主题
         */
        @StyleRes
        public int theme;
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
