package androidz;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentActivity;


public class LoadingDialog extends AppCompatDialogFragment {
    private static final String LOADING_LAYOUT = "loading_layout";
    private static final String LOADING_MESSAGE = "loading_message";

    private static LoadingDialog loadingDialog;

    public static void showLoading(@NonNull FragmentActivity activity) {
        showLoading(activity, new Options());
    }

    public static void showLoading(@NonNull FragmentActivity activity, @Nullable Options options) {
        hide();
        if (options == null) {
            options = new Options();
        }
        Bundle arguments = new Bundle();
        if (options.loadingRes != 0) {
            arguments.putInt(LOADING_LAYOUT, options.loadingRes);
        }
        if (options.messageRes != 0) {
            arguments.putString(LOADING_MESSAGE, activity.getString(options.messageRes));
        }
        if (options.message != null) {
            arguments.putString(LOADING_MESSAGE, options.message);
        }
        loadingDialog = new LoadingDialog();
        loadingDialog.setArguments(arguments);
        loadingDialog.setCancelable(options.cancelable);
        loadingDialog.show(activity.getSupportFragmentManager(), null);
    }

    public static void hide() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        loadingDialog = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            int contentId = arguments.getInt(LOADING_LAYOUT, R.layout.loading_dialog);
            return inflater.inflate(contentId, container, false);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            CharSequence message = arguments.getCharSequence(LOADING_MESSAGE);
            if (message != null) {
                TextView textView = view.findViewById(R.id.loading_message);
                textView.setText(message);
            }
        }
    }

    public static class Options {
        @LayoutRes
        public int loadingRes;
        public String message;
        @StringRes
        public int messageRes;
        public boolean cancelable;
    }
}