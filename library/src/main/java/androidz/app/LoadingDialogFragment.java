package androidz.app;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ziojio.androidz.R;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * 屏幕旋转时保持状态
 */
public class LoadingDialogFragment extends AppCompatDialogFragment {

    private static final String LOADING_LAYOUT_RES = "loading_layout_res";
    private static final String LOADING_MESSAGE = "loading_message";
    private static final String LOADING_PROGRESS_COLOR = "loading_progress_color";

    public LoadingDialogFragment() {
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int contentLayoutId = requireArguments().getInt(LOADING_LAYOUT_RES, R.layout.loading_dialog);
        return inflater.inflate(contentLayoutId, new FrameLayout(requireContext()), false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = requireArguments();
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

    public void setContentView(@LayoutRes int layoutResID) {
        requireArguments().putInt(LOADING_LAYOUT_RES, layoutResID);
    }

    public void setMessage(CharSequence text) {
        requireArguments().putCharSequence(LOADING_MESSAGE, text);
    }

    public void setProgressColor(@ColorInt int progressColor) {
        requireArguments().putInt(LOADING_PROGRESS_COLOR, progressColor);
    }
}