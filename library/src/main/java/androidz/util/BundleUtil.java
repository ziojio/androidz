package androidz.util;

import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BundleUtil {

    public static Map<String, Object> toMap(Bundle bundle) {
        if (bundle == null) return null;
        Map<String, Object> data = new HashMap<>();
        for (String key : bundle.keySet()) {
            if (key == null) continue;

            Object value = bundle.get(key);
            if (value instanceof Bundle) {
                data.put(key, toMap((Bundle) value));
            } else {
                data.put(key, value);
            }
        }
        return data;
    }

    public static Bundle fromMap(Map<String, Object> map) {
        if (map == null) return null;
        Bundle bundle = new Bundle();
        for (String key : map.keySet()) {
            if (key == null) continue;

            Object value = map.get(key);
            if (value == null) {
                bundle.putString(key, null);
            } else if (value instanceof Serializable) {
                bundle.putSerializable(key, (Serializable) value);
            } else if (value instanceof CharSequence) {
                bundle.putCharSequence(key, (CharSequence) value);
            } else if (value instanceof Parcelable) {
                bundle.putParcelable(key, (Parcelable) value);
            } else {
                bundle.putString(key, value.toString());
            }
        }
        return bundle;
    }

}
