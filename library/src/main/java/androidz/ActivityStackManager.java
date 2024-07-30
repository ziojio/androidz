package androidz;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayDeque;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Activity 管理
 */
public final class ActivityStackManager implements ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";

    private static final ActivityStackManager INSTANCE = new ActivityStackManager();

    public static ActivityStackManager getInstance() {
        return INSTANCE;
    }

    private final ArrayDeque<Activity> stack = new ArrayDeque<>();
    private Application application;

    public boolean registered() {
        return application != null;
    }

    public synchronized void register(@NonNull Application app) {
        if (application == null) {
            application = app;
            app.registerActivityLifecycleCallbacks(this);
        }
    }

    public synchronized void unregister() {
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
            application = null;
            stack.clear();
        }
    }

    @Nullable
    public Activity getTopActivity() {
        return stack.peek();
    }

    public void finishTopActivity() {
        Activity activity = stack.peek();
        if (activity != null) {
            activity.finish();
        }
    }

    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
    }

    /**
     * 回到首页
     */
    public void popToRootActivity(boolean animated) {
        popBack(stack.size() - 1, animated);
    }

    /**
     * 回退指定数量的页面
     */
    public void popBack(@IntRange(from = 1) int count, boolean animated) {
        if (count <= 0) {
            return;
        }
        int size = Math.min(stack.size(), count);
        for (int i = 0; i < size; i++) {
            if (!stack.isEmpty()) {
                Activity activity = stack.pop();
                activity.finish();

                if (!animated) {
                    // 无动画
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        activity.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0);
                    } else {
                        activity.overridePendingTransition(0, 0);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        stack.push(activity);
        if (Androidz.isDebuggable()) {
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) {
                bundle.getString("");
            }
            Log.d(TAG, "onActivityCreated: " + activity + " " + bundle);
            printActivityStack();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityStarted: " + activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityResumed: " + activity);
        }
        if (activity != stack.peek()) {
            stack.remove(activity);
            stack.push(activity);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityPaused: " + activity);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityStopped: " + activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        stack.remove(activity);
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
            printActivityStack();
        }
    }

    private void printActivityStack() {
        Log.d(TAG, "----------- stack start -----------");
        for (Activity activity : stack) {
            Log.d(TAG, "|\t" + activity);
        }
        Log.d(TAG, "------------ stack end ------------");
    }
}