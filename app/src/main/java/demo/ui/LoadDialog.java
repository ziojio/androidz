package demo.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import androidz.app.AppDialog;
import demo.R;

public class LoadDialog extends AppDialog {

    public LoadDialog(Context context) {
        super(context);
    }

    public LoadDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load);
    }

    public static class Builder extends AppDialog.Builder<Builder, LoadDialog> {

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeId) {
            super(context, themeId);
        }

        @Override
        protected LoadDialog createDialog(@NonNull Context context, int themeId) {
            return new LoadDialog(context, themeId);
        }

        //
        // public Builder setContentView(@LayoutRes int resId) {
        //     return setContentView(LayoutInflater.from(getContext()).inflate(resId, mContainerLayout, false));
        // }
        //
        // public Builder setContentView(View view) {
        //     mContainerLayout.addView(view, 1);
        //     return this;
        // }

    }

}
