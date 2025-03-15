package androidz.util;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED;

import android.annotation.SuppressLint;
import android.app.Activity;
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
    private static InternalDialog dialog;

    public static void showLoading(@NonNull Activity activity) {
        showLoading(activity, new Options());
    }

    public static void showLoading(@NonNull Activity activity, @NonNull Options options) {
        hide();
        dialog = new InternalDialog(activity, options);
        // 主动取消情况下不持有弹窗
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

    public static final class InternalDialog extends AppCompatDialog {
        private final Activity activity;
        private final Options options;
        private int orientation = SCREEN_ORIENTATION_LOCKED;

        public InternalDialog(@NonNull Activity activity, @NonNull Options options) {
            super(activity);
            this.activity = activity;
            this.options = options;
            setCancelable(options.cancelable);
            setCanceledOnTouchOutside(options.touchOutsideCancelable);
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
        protected void onStart() {
            super.onStart();
            orientation = activity.getRequestedOrientation();
            if (orientation != SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(SCREEN_ORIENTATION_LOCKED);
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            if (orientation != SCREEN_ORIENTATION_LOCKED) {
                activity.setRequestedOrientation(orientation);
                orientation = SCREEN_ORIENTATION_LOCKED;
            }
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
        public boolean touchOutsideCancelable = false;
    }
}
