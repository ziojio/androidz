package androidz.app;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

public class ApplicationInstance implements ViewModelStoreOwner {

    private ViewModelStore mViewModelStore;

    private static final ApplicationInstance sInstance = new ApplicationInstance();

    public static ApplicationInstance getInstance() {
        return sInstance;
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (mViewModelStore == null) {
            mViewModelStore = new ViewModelStore();
        }
        return mViewModelStore;
    }
}