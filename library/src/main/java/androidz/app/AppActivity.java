package androidz.app;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class AppActivity extends AppCompatActivity {
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeCallbacks();
    }

    protected <T extends ViewModel> T getActivityViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(this).get(modelClass);
    }

    protected <T extends ViewModel> T getApplicationViewModel(@NonNull Class<T> modelClass) {
        return new ViewModelProvider(ApplicationInstance.getInstance()).get(modelClass);
    }

    /**
     * 获取设置的内容视图
     *
     * @see #setContentView
     */
    protected View getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
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
