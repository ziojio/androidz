package androidz.app;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AppFragment extends Fragment {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeCallbacks();
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

    /**
     * 获取 Main Handler
     */
    protected Handler getHandler() {
        return mHandler;
    }

    protected boolean post(Runnable runnable) {
        return mHandler.postAtTime(runnable, this, SystemClock.uptimeMillis());
    }

    protected boolean postDelayed(Runnable runnable, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return mHandler.postAtTime(runnable, this, SystemClock.uptimeMillis() + delayMillis);
    }

    protected void removeCallbacks(Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }

    protected void removeCallbacks() {
        // 移除和当前对象相关的消息回调
        mHandler.removeCallbacksAndMessages(this);
    }
}
