package androidz.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;

import androidx.annotation.NonNull;

import static androidz.util.Androidz.isDebuggable;

/**
 * Activity 管理
 */
public class ActivityStackManager implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";

    private final ArrayDeque<Activity> stack = new ArrayDeque<>();

    private Application application;

    private ActivityStackManager() {
    }

    private static final class Instance {
        static final ActivityStackManager sInstance = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return Instance.sInstance;
    }

    public synchronized void register(@NonNull Application app) {
        if (application != null) {
            throw new IllegalStateException("ActivityStackManager already registered!");
        }
        application = app;
        app.registerActivityLifecycleCallbacks(this);
    }

    public synchronized void unregister() {
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
            stack.clear();
            application = null;
        }
    }

    private void addActivity(Activity activity) {
        stack.push(activity);
    }

    private void removeActivity(Activity activity) {
        if (!stack.isEmpty()) {
            stack.remove(activity);
        }
    }

    public Activity getTopActivity() {
        if (!stack.isEmpty()) {
            return stack.peek();
        }
        return null;
    }

    public void finishTopActivity() {
        if (!stack.isEmpty()) {
            Activity activity = stack.peek();
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 销毁除了RootActivity之外的所有activity
     */
    public void popToRootActivity(boolean animated) {
        while (stack.size() > 1) {
            Activity activity = stack.pop();
            if (activity != null) {
                activity.finish();
            }

            // 无动画
            if (!animated) {
                if (activity != null) {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
    }

    /**
     * 向前回退指定数量的页面
     */
    public void popBack(int count, boolean animated) {
        if (count < 1) {
            count = 1;
        }
        if (count > stack.size()) {
            count = stack.size();
        }

        for (int i = 0; i < count; i++) {
            if (!stack.isEmpty()) {
                Activity activity = stack.pop();
                if (activity != null) {
                    activity.finish();
                }

                // 无动画
                if (!animated) {
                    if (activity != null) {
                        activity.overridePendingTransition(0, 0);
                    }
                }
            }
        }
    }

    /**
     * 销毁所有activity
     */
    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
        if (isDebuggable()) {
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) bundle.getString("");
            Log.d(TAG, "onActivityCreated: " + activity + " " + bundle);
            printActivityStack(stack);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (isDebuggable()) {
            Log.d(TAG, "onActivityStarted: " + activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (isDebuggable()) {
            Log.d(TAG, "onActivityResumed: " + activity);
        }
        if (activity != stack.peek()) {
            removeActivity(activity);
            addActivity(activity);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (isDebuggable()) {
            Log.d(TAG, "onActivityPaused: " + activity);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (isDebuggable()) {
            Log.d(TAG, "onActivityStopped: " + activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        removeActivity(activity);
        if (isDebuggable()) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
            printActivityStack(stack);
        }
    }

    private void printActivityStack(Deque<Activity> stack) {
        Log.d(TAG, "----------- stack start -----------");
        for (Activity activity : stack) {
            Log.d(TAG, "|\t" + activity);
        }
        Log.d(TAG, "----------- stack end -----------");
    }
}