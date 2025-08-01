package androidz.util;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * 1.获取最近显示的Activity
 * 2.结束所有Activity
 * 3.判断应用是否处于后台
 */
@SuppressWarnings("unused")
public final class ActivityLifecycleManager implements ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityLifecycle";
    private static final String TAG_STACK = "ActivityStack";

    private final ArrayList<Activity> stack;
    private final ArrayList<Activity> startedStack;

    public ActivityLifecycleManager() {
        stack = new ArrayList<>(8);
        startedStack = new ArrayList<>(4);
    }

    public boolean isBackground() {
        return this.startedStack.isEmpty();
    }

    public Activity getTopActivity() {
        int size = stack.size();
        if (size > 0) {
            return stack.get(size - 1);
        }
        return null;
    }

    public void finishAllActivity() {
        for (int size = stack.size(); size > 0; size = stack.size()) {
            Activity activity = stack.remove(size - 1);
            activity.finish();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        stack.add(activity);
        if (UtilApp.isLoggable()) {
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) {
                bundle.size();
                Log.d(TAG, "onActivityCreated: " + activity + " " + bundle);
            } else {
                Log.d(TAG, "onActivityCreated: " + activity);
            }
            printActivityStack();
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        startedStack.add(activity);
        if (UtilApp.isLoggable()) {
            Log.d(TAG, "onActivityStarted: " + activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (activity != stack.get(stack.size() - 1)) {
            // 更新当前显示的Activity
            stack.remove(activity);
            stack.add(activity);
        }
        if (UtilApp.isLoggable()) {
            Log.d(TAG, "onActivityResumed: " + activity);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (UtilApp.isLoggable()) {
            Log.d(TAG, "onActivityPaused: " + activity);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        startedStack.remove(activity);
        if (UtilApp.isLoggable()) {
            Log.d(TAG, "onActivityStopped: " + activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        stack.remove(activity);
        if (UtilApp.isLoggable()) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
            printActivityStack();
        }
    }

    private void printActivityStack() {
        Log.d(TAG_STACK, "--------------------------------");
        for (Activity activity : stack) {
            Log.d(TAG_STACK, "|\t" + activity);
        }
        Log.d(TAG_STACK, "--------------------------------");
    }
}