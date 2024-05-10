package androidz

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.IntRange
import java.util.ArrayDeque
import kotlin.math.min

/**
 * Activity 管理
 */
object ActivityStackManager : ActivityLifecycleCallbacks {
    private const val TAG = "ActivityStackManager"

    private val stack = ArrayDeque<Activity>()

    private var application: Application? = null

    @JvmStatic
    var isDebuggable: Boolean = false

    @JvmStatic
    val isRegistered: Boolean
        get() = application != null

    @JvmStatic
    fun register(app: Application) {
        synchronized(this) {
            if (application == null) {
                application = app
                app.registerActivityLifecycleCallbacks(this)
            }
        }
    }

    @JvmStatic
    fun unregister() {
        synchronized(this) {
            if (application != null) {
                application!!.unregisterActivityLifecycleCallbacks(this)
                application = null
                stack.clear()
            }
        }
    }

    private fun addActivity(activity: Activity) {
        stack.push(activity)
    }

    private fun removeActivity(activity: Activity) {
        if (!stack.isEmpty()) {
            stack.remove(activity)
        }
    }

    @JvmStatic
    val topActivity: Activity?
        get() {
            if (!stack.isEmpty()) {
                return stack.peek()
            }
            return null
        }

    @JvmStatic
    fun finishTopActivity() {
        if (!stack.isEmpty()) {
            val activity = stack.peek()
            activity?.finish()
        }
    }

    /**
     * 销毁除了 RootActivity 之外的所有 Activity
     */
    @JvmStatic
    @JvmOverloads
    fun popToRootActivity(animated: Boolean = true) {
        while (stack.size > 1) {
            val activity = stack.pop()
            activity?.finish()

            // 无动画
            if (!animated) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    activity?.overrideActivityTransition(
                        Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0
                    )
                } else {
                    activity?.overridePendingTransition(0, 0)
                }
            }
        }
    }

    /**
     * 回退指定数量的页面
     */
    @JvmStatic
    @JvmOverloads
    fun popBack(@IntRange(from = 1) count: Int = 1, animated: Boolean = true) {
        check(count > 0) { "count[$count] must be greater than 0" }
        val size = min(stack.size, count)
        for (i in 0 until size) {
            if (!stack.isEmpty()) {
                val activity = stack.pop()
                activity?.finish()

                // 无动画
                if (!animated) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                        activity?.overrideActivityTransition(
                            Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0
                        )
                    } else {
                        activity?.overridePendingTransition(0, 0)
                    }
                }
            }
        }
    }

    /**
     * 销毁所有 Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        while (!stack.isEmpty()) {
            stack.pop().finish()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        addActivity(activity)
        if (isDebuggable) {
            val bundle = activity.intent.extras
            bundle?.getString("")
            Log.d(TAG, "onActivityCreated: $activity $bundle")
            printActivityStack()
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (isDebuggable) {
            Log.d(TAG, "onActivityStarted: $activity")
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (isDebuggable) {
            Log.d(TAG, "onActivityResumed: $activity")
        }
        if (activity !== stack.peek()) {
            removeActivity(activity)
            addActivity(activity)
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isDebuggable) {
            Log.d(TAG, "onActivityPaused: $activity")
        }
    }

    override fun onActivityStopped(activity: Activity) {
        if (isDebuggable) {
            Log.d(TAG, "onActivityStopped: $activity")
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        removeActivity(activity)
        if (isDebuggable) {
            Log.d(TAG, "onActivityDestroyed: $activity")
            printActivityStack()
        }
    }

    private fun printActivityStack() {
        Log.d(TAG, "----------- stack start -----------")
        for (activity in stack) {
            Log.d(TAG, "|\t$activity")
        }
        Log.d(TAG, "------------ stack end ------------")
    }

}