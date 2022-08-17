package androidz.util;

import android.content.Context;
import android.util.TypedValue;

import androidx.annotation.AttrRes;

public class ResUtil {

    public static int getAttrColor(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, true);
        return typedValue.data;
    }

    public static int getAttrDrawable(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(attr, typedValue, false);
        return typedValue.data;
    }


}
