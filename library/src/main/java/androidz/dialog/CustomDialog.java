package androidz.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import androidz.app.AppDialog;

/**
 * 通过Builder构造Dialog。
 */
public final class CustomDialog extends AppDialog {

    private CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder extends AppDialog.Builder<Builder, CustomDialog> {

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeId) {
            super(context, themeId);
        }

        @Override
        protected CustomDialog createDialog(@NonNull Context context, int themeId) {
            return new CustomDialog(context, themeId);
        }

        public Builder setContentView(@LayoutRes int resId) {
            return setContentView(LayoutInflater.from(getContext()).inflate(resId, new FrameLayout(getContext()), false));
        }

        public Builder setContentView(@NonNull View view) {
            getDialog().setContentView(view);
            return this;
        }

        public Builder setOnClickListener(@IdRes int id, View.OnClickListener listener) {
            getDialog().setOnClickListener(listener, id);
            return this;
        }

        public Builder setOnClickListener(View.OnClickListener listener, @IdRes int... ids) {
            getDialog().setOnClickListener(listener, ids);
            return this;
        }
    }

}