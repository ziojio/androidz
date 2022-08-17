package androidz.util;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;


public final class LoadingDialog {

    private static final int LOADING_VIEW = R.layout.loading_dialog;

    private static InternalDialog loadingDialog;

    public static void showLoading(@NonNull Context context) {
        Options options = new Options();
        showLoading(context, options);
    }

    public static void showLoading(@NonNull Context context, @NonNull Options options) {
        hide();

        loadingDialog = new InternalDialog(context, options);
        loadingDialog.show();
    }

    public static void hide() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public static boolean isShowing() {
        if (loadingDialog != null) {
            return loadingDialog.isShowing();
        }
        return false;
    }

    public static class Options {
        @LayoutRes
        public int loadingRes;
        @StringRes
        public int messageRes;
        public String message;
        public boolean cancelable;
        public DialogInterface.OnShowListener onShowListener;
        public DialogInterface.OnDismissListener onDismissListener;
    }

    public static class InternalDialog extends AppCompatDialog {
        private final Options options;

        public InternalDialog(@NonNull Context context, @NonNull Options options) {
            super(context);
            this.options = options;
        }

        public InternalDialog(@NonNull Context context, int theme, @NonNull Options options) {
            super(context, theme);
            this.options = options;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(options.loadingRes != 0 ? options.loadingRes : LOADING_VIEW);
            setCancelable(options.cancelable);
            if (options.onShowListener != null) {
                setOnShowListener(options.onShowListener);
            }
            if (options.onDismissListener != null) {
                setOnDismissListener(options.onDismissListener);
            }

            TextView textView = findViewById(R.id.loading_message);
            if (textView != null) {
                if (options.message != null) {
                    textView.setText(options.message);
                } else if (options.messageRes != 0) {
                    textView.setText(options.messageRes);
                }
            }
        }
    }
}