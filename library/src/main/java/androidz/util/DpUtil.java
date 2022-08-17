package androidz.util;

import android.content.Context;

public class DpUtil {

    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static float dp2pxF(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }

    public static int px2dp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static float px2dpF(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return px / scale;
    }


    public static int px2sp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    public static float px2spF(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scale;
    }

    public static int sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static float sp2pxF(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

}
