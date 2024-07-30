package androidz.util;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.Log;

import java.util.Stack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class ActivityStackManager implements ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";

    private static final class Instance {
        private static final ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return Instance.INSTANCE;
    }

    private final Stack<Activity> stack = new Stack<>();
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

    public Activity getTopActivity() {
        return stack.peek();
    }

    public void finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish();
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        stack.push(activity);
        if (Androidz.isDebuggable()) {
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) {
                bundle.get("");
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