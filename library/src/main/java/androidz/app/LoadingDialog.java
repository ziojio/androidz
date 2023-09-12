package androidz.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziojio.androidz.R;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDialog;

public class LoadingDialog extends AppCompatDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

    public void setProgressColor(@ColorInt int progressColor) {
        ProgressBar progressBar = findViewById(R.id.loading_progress_bar);
        if (progressBar != null) {
            progressBar.setIndeterminateTintList(ColorStateList.valueOf(progressColor));
        }
    }
}