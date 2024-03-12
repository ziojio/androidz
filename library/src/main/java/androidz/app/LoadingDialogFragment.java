package androidz.app;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziojio.androidz.R;

import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

/**
 * 屏幕旋转时保持状态
 */
public class LoadingDialogFragment extends AppCompatDialogFragment {

    private static final String LOADING_LAYOUT_RES = "loading_layout_res";
    private static final String LOADING_MESSAGE = "loading_message";
    private static final String LOADING_MESSAGE_RES = "loading_message_res";
    private static final String LOADING_PROGRESS_COLOR_RES = "loading_progress_color_res";

    public LoadingDialogFragment() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            container = new FrameLayout(requireActivity());
        }
        int contentLayoutId = requireArguments().getInt(LOADING_LAYOUT_RES, R.layout.loading_dialog);
        return inflater.inflate(contentLayoutId, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = requireArguments();

        TextView textView = view.findViewById(R.id.loading_message);
        if (textView != null) {
            CharSequence message = arguments.getCharSequence(LOADING_MESSAGE);
            if (message != null) {
                textView.setText(message);
            } else {
                int messageRes = arguments.getInt(LOADING_MESSAGE_RES);
                if (messageRes != Resources.ID_NULL) {
                    textView.setText(messageRes);
                }
            }
        }
        ProgressBar progressBar = view.findViewById(R.id.loading_progress_bar);
        if (progressBar != null) {
            int progressColorRes = arguments.getInt(LOADING_PROGRESS_COLOR_RES);
            if (progressColorRes != Resources.ID_NULL) {
                ColorStateList colorStateList = ContextCompat.getColorStateList(requireActivity(), progressColorRes);
                if (colorStateList != null) {
                    progressBar.setIndeterminateTintList(colorStateList);
                }
            }
        }
    }

    public void setContentView(@LayoutRes int layoutResID) {
        requireArguments().putInt(LOADING_LAYOUT_RES, layoutResID);
    }

    public void setMessage(CharSequence text) {
        requireArguments().putCharSequence(LOADING_MESSAGE, text);
    }

    public void setMessage(@StringRes int resId) {
        requireArguments().putInt(LOADING_MESSAGE_RES, resId);
    }

    public void setProgressColor(@ColorRes int resId) {
        requireArguments().putInt(LOADING_PROGRESS_COLOR_RES, resId);
    }
}