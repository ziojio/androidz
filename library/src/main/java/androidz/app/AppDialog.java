package androidz.app;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;

import androidz.action.ClickAction;

/**
 * @see androidz.dialog.CommonDialog
 * @see androidz.dialog.CustomDialog
 */
public class AppDialog extends AppCompatDialog implements ClickAction {

    public AppDialog(Context context) {
        this(context, 0);
    }

    public AppDialog(Context context, int theme) {
        super(context, theme);
    }

    protected AppDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * @noinspection unchecked
     */
    public static abstract class Builder<B extends Builder<?, ?>, D extends Dialog> {
        private final Context mContext;
        private final D mDialog;

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeId) {
            mContext = context;
            mDialog = createDialog(mContext, themeId);
            mDialog.create();
        }

        protected abstract D createDialog(@NonNull Context context, @StyleRes int themeId);

        public Context getContext() {
            return mContext;
        }

        public D getDialog() {
            return mDialog;
        }

        protected <V extends View> V findViewById(@IdRes int id) {
            return mDialog.findViewById(id);
        }

        public B setOnShowListener(DialogInterface.OnShowListener listener) {
            mDialog.setOnShowListener(listener);
            return (B) this;
        }

        public B setOnCancelListener(DialogInterface.OnCancelListener listener) {
            mDialog.setOnCancelListener(listener);
            return (B) this;
        }

        public B setOnDismissListener(DialogInterface.OnDismissListener listener) {
            mDialog.setOnDismissListener(listener);
            return (B) this;
        }

        public B setCancelable(boolean flag) {
            mDialog.setCancelable(flag);
            return (B) this;
        }

        public B setCanceledOnTouchOutside(boolean cancel) {
            mDialog.setCanceledOnTouchOutside(cancel);
            return (B) this;
        }

        public B setLayout(int width, int height) {
            mDialog.getWindow().setLayout(width, height);
            return (B) this;
        }

        public B setGravity(int gravity) {
            mDialog.getWindow().setGravity(gravity);
            return (B) this;
        }

        public B setWindowAnimations(@StyleRes int resId) {
            mDialog.getWindow().setWindowAnimations(resId);
            return (B) this;
        }

        public B setWindowDimEnabled(boolean enabled) {
            Window window = mDialog.getWindow();
            if (enabled) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
            return (B) this;
        }

        public B setDimAmount(@FloatRange(from = 0.0, to = 1.0) float dimAmount) {
            mDialog.getWindow().setDimAmount(dimAmount);
            return (B) this;
        }

        public void show() {
            mDialog.show();
        }
    }
}
