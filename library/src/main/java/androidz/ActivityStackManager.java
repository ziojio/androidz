package androidz;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Activity栈管理
 */
public class ActivityStackManager implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "ActivityStackManager";

    public static final String PAGE_ID = "PAGE_ID";

    private final ArrayDeque<Activity> stack;
    private Application application;

    private ActivityStackManager() {
        stack = new ArrayDeque<>();
    }

    private static final class Instance {
        static final ActivityStackManager sInstance = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return Instance.sInstance;
    }

    public void register(@NonNull Application app) {
        if (application != null) {
            throw new IllegalStateException("ActivityStackManager already registered");
        }
        application = app;
        app.registerActivityLifecycleCallbacks(this);
    }

    public void unregister() {
        if (application != null) {
            application.unregisterActivityLifecycleCallbacks(this);
            stack.clear();
            application = null;
        }
    }

    private boolean isDebug() {
        return Androidz.isDebug();
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

    public void finishActivity(String pageId) {
        Activity activity = getActivity(pageId);
        if (activity != null) {
            activity.finish();
        }
    }

    public Activity getActivity(String pageId) {
        if (TextUtils.isEmpty(pageId)) {
            return null;
        }
        for (Activity activity : stack) {
            String activityPageId = getPageIdFromActivity(activity);
            if (pageId.equals(activityPageId)) {
                return activity;
            }
        }
        return null;
    }

    /**
     * 销毁pageId所对应的Activity之上的所有activity
     */
    public void popToActivity(String pageId, boolean animated) {
        if (TextUtils.isEmpty(pageId)) {
            return;
        }

        int count = 0;
        boolean matched = false;
        for (Activity activity : stack) {
            String activityPageId = getPageIdFromActivity(activity);
            if (pageId.equals(activityPageId)) {
                matched = true;
                break;
            }
            count++;
        }

        if (matched) {
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

    private String getPageIdFromActivity(Activity activity) {
        if (activity == null) {
            return null;
        }
        String pageId = null;
        if (activity.getIntent() != null) {
            try {
                pageId = activity.getIntent().getStringExtra(PAGE_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pageId == null) {
            pageId = activity.toString();
        }
        return pageId;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        addActivity(activity);
        if (isDebug()) {
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle != null) bundle.getString(PAGE_ID);
            Log.d(TAG, "onActivityCreated: " + activity + " " + bundle);
            printActivityStack(stack);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (isDebug()) {
            Log.d(TAG, "onActivityStarted: " + activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isDebug()) {
            Log.d(TAG, "onActivityResumed: " + activity);
        }
        if (activity != stack.peek()) {
            removeActivity(activity);
            addActivity(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (isDebug()) {
            Log.d(TAG, "onActivityPaused: " + activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (isDebug()) {
            Log.d(TAG, "onActivityStopped: " + activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        removeActivity(activity);
        if (isDebug()) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
            printActivityStack(stack);
        }
    }

    private void printActivityStack(Deque<Activity> stack) {
        Log.d(TAG, "----------- stack start -----------");
        for (Activity activity : stack) {
            String activityPageId = getPageIdFromActivity(activity);
            Log.d(TAG, "|\t" + activity + " -> " + activityPageId);
        }
        Log.d(TAG, "----------- stack end -----------");
    }
}