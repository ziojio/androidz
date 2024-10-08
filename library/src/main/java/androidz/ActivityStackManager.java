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
public class ActivityStackManager implements ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";

    public static final ActivityStackManager INSTANCE = new ActivityStackManager();

    private final ArrayDeque<Activity> stack = new ArrayDeque<>();

    private Application application;

    public boolean isRegistered() {
        return application != null;
    }

    public void register(Application app) {
        synchronized (this) {
            if (application == null) {
                application = app;
                app.registerActivityLifecycleCallbacks(this);
            }
        }
    }

    public void unregister() {
        synchronized (this) {
            if (application != null) {
                application.unregisterActivityLifecycleCallbacks(this);
                application = null;
                stack.clear();
            }
        }
    }

    private void addActivity(Activity activity) {
        stack.push(activity);
    }

    private void removeActivity(Activity activity) {
        stack.remove(activity);
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
     * 销毁除了 RootActivity 之外的所有 Activity
     */
    public void popToRootActivity(boolean animated) {
        while (stack.size() > 1) {
            Activity activity = stack.pop();
            activity.finish();

            if (!animated) {
                // 无动画
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    activity.overrideActivityTransition(
                            Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0
                    );
                } else {
                    activity.overridePendingTransition(0, 0);
                }
            }
        }
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
                        activity.overrideActivityTransition(
                                Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0
                        );
                    } else {
                        activity.overridePendingTransition(0, 0);
                    }
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        addActivity(activity);
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
            removeActivity(activity);
            addActivity(activity);
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
        removeActivity(activity);
        if (Androidz.isDebuggable()) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
            printActivityStack();
        }
    }

    private void printActivityStack() {
        Log.d(TAG, "----------- stack start -----------");
        for (Activity activity : stack) {
            Log.d(TAG, "|\t$activity");
        }
        Log.d(TAG, "------------ stack end ------------");
    }
}