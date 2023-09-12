package androidz.app;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidz.action.HandlerAction;

public class AppFragment extends Fragment implements HandlerAction {

    protected <T extends ViewModel> T getFragmentViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(requireActivity()).get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(ApplicationInstance.getInstance()).get(modelClass);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCallbacks();
    }
}
