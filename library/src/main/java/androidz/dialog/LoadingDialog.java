package androidz.dialog;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.ziojio.androidz.R;

import androidz.app.AppDialogFragment;

/**
 * 使用 DialogFragment, 旋转屏幕时保持状态
 */
public class LoadingDialog extends AppDialogFragment {

    private static final String LOADING_LAYOUT_RES = "loading_layout_res";
    private static final String LOADING_MESSAGE = "loading_message";
    private static final String LOADING_PROGRESS_COLOR = "loading_progress_color";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int contentLayoutId = getArguments() != null ? getArguments().getInt(LOADING_LAYOUT_RES) : R.layout.loading_dialog;
        return inflater.inflate(contentLayoutId, new FrameLayout(requireContext()), false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            CharSequence message = arguments.getCharSequence(LOADING_MESSAGE);
            int progressColor = arguments.getInt(LOADING_PROGRESS_COLOR);
            if (message != null) {
                TextView textView = view.findViewById(R.id.loading_message);
                if (textView != null) {
                    textView.setText(message);
                }
            }
            if (progressColor != 0) {
                ProgressBar progressBar = view.findViewById(R.id.loading_progress_bar);
                if (progressBar != null) {
                    progressBar.setIndeterminateTintList(ColorStateList.valueOf(progressColor));
                }
            }
        }
    }

    public static final class Builder {
        private final FragmentActivity mActivity;
        private final Bundle mArguments = new Bundle();
        private boolean cancelable = false;

        public Builder(@NonNull FragmentActivity activity) {
            mActivity = activity;
        }

        public Builder setMessage(@StringRes int resId) {
            return setMessage(mActivity.getString(resId));
        }

        public Builder setMessage(CharSequence text) {
            mArguments.putCharSequence(LOADING_MESSAGE, text);
            return this;
        }

        public Builder setProgressColor(@ColorInt int progressColor) {
            mArguments.putInt(LOADING_PROGRESS_COLOR, progressColor);
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public LoadingDialog create() {
            LoadingDialog loadingDialog = new LoadingDialog();
            loadingDialog.setCancelable(cancelable);
            if (!mArguments.isEmpty()) {
                loadingDialog.setArguments(mArguments);
            }
            return loadingDialog;
        }

        public void show() {
            LoadingDialog loadingDialog = create();
            loadingDialog.show(mActivity.getSupportFragmentManager(), null);
        }
    }
}