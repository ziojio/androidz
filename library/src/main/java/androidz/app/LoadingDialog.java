package androidz.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziojio.androidz.R;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

public class LoadingDialog extends AppCompatDialog {

    public LoadingDialog(Context context) {
        super(context);
        create();
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        create();
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        create();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
    }

    public void setMessage(CharSequence text) {
        TextView textView = findViewById(R.id.loading_message);
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setMessage(@StringRes int resId) {
        setMessage(getContext().getText(resId));
    }

    public void setProgressColor(ColorStateList colorStateList) {
        ProgressBar progressBar = findViewById(R.id.loading_progress_bar);
        if (progressBar != null && colorStateList != null) {
            progressBar.setIndeterminateTintList(colorStateList);
        }
    }

    public void setProgressColor(@ColorRes int resId) {
        setProgressColor(ContextCompat.getColorStateList(getContext(), resId));
    }
}