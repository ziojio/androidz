package androidz.dialog;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.ziojio.androidz.R;


public final class MessageDialog {

    public static final class Builder extends CommonDialog.Builder<Builder> {
        private final TextView mMessageView;

        public Builder(Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, int themeId) {
            super(context, themeId);
            setContentView(R.layout.message_dialog);
            mMessageView = findViewById(R.id.message);
        }

        public Builder setMessage(@StringRes int resId) {
            return setMessage(getContext().getString(resId));
        }

        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            return this;
        }
    }
}