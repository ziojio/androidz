package androidz.util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

public class ScreenUtil {

    public static DisplayMetrics getDisplayMetrics(@NonNull Context context) {
        WindowManager wm = getSystemService(context, WindowManager.class);
        DisplayMetrics dm = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getRealMetrics(dm);
        }
        return dm;
    }

    public Point getLocationOnScreen(@NonNull View view) {
        int[] point = new int[2];
        view.getLocationOnScreen(point);
        return new Point(point[0], point[1]);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void setLandscape(@NonNull Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void setPortrait(@NonNull Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static boolean isLandscape(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isPortrait(@NonNull Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int getScreenRotation(@NonNull Activity activity) {
        int rotation;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            rotation = activity.getDisplay().getRotation();
        } else {
            rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        }
        if (rotation == Surface.ROTATION_90) {
            return 90;
        } else if (rotation == Surface.ROTATION_180) {
            return 180;
        } else if (rotation == Surface.ROTATION_270) {
            return 270;
        }
        return 0;
    }

    /**
     * Set the duration of sleep.
     */
    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public static void setSleepDuration(@NonNull Context context, int duration) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, duration);
    }

}
