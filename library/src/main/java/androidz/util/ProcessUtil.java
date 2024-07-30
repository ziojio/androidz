package androidz.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public final class ProcessUtil {

    public static boolean isMainProcess(@NonNull Context context) {
        String processName = getProcessName(context);
        ApplicationInfo info = context.getApplicationInfo();
        return TextUtils.equals(processName, info.processName);
    }

    /**
     * @return The name of the active process.
     */
    @Nullable
    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static String getProcessName(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            return Application.getProcessName();
        }

        // Try using ActivityThread to determine the current process name.
        try {
            Class<?> activityThread = Class.forName(
                    "android.app.ActivityThread",
                    false,
                    ProcessUtil.class.getClassLoader());
            final Object packageName;
            Method currentProcessName = activityThread.getDeclaredMethod("currentProcessName");
            currentProcessName.setAccessible(true);
            packageName = currentProcessName.invoke(null);
            if (packageName instanceof String) {
                return (String) packageName;
            }
        } catch (Throwable exception) {
            Log.d("ProcessUtil", "Unable to check ActivityThread for processName", exception);
        }

        // Fallback to the most expensive way
        int pid = Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            if (processes != null && !processes.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo process : processes) {
                    if (process.pid == pid) {
                        return process.processName;
                    }
                }
            }
        }

        return null;
    }
}