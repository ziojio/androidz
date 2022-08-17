package androidz.app;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import androidz.action.ClickAction;

/**
 * @see androidz.dialog.LoadingDialog
 */
public class AppDialogFragment extends AppCompatDialogFragment implements DialogInterface, ClickAction {
    private static final String DIALOG_CONTENT_LAYOUT_ID = "AppDialog:content_layout_id";

    @Override
    public void cancel() {
        requireDialog().cancel();
    }

    @Override
    public <V extends View> V findViewById(int id) {
        return requireView().findViewById(id);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int contentLayoutId = getArguments() == null ? 0 : getArguments().getInt(DIALOG_CONTENT_LAYOUT_ID);
        if (contentLayoutId != 0) {
            if (container == null) {
                container = new FrameLayout(inflater.getContext());
            }
            return inflater.inflate(contentLayoutId, container, false);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    /**
     * Similar to {@link Dialog#setContentView(int)}
     */
    public void setContentView(@LayoutRes int layoutResID) {
        if (getArguments() != null) {
            if (isStateSaved()) {
                throw new IllegalStateException("Fragment already added and state has been saved");
            }
            getArguments().putInt(DIALOG_CONTENT_LAYOUT_ID, layoutResID);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(DIALOG_CONTENT_LAYOUT_ID, layoutResID);
            setArguments(bundle);
        }
    }

    protected <T extends ViewModel> T getFragmentViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(requireActivity()).get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(ApplicationInstance.getInstance()).get(modelClass);
    }

}
