package androidz.util;

import android.os.Build;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class UtilSystem {

    public static Map<String, Object> buildInfo() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("brand", Build.BRAND);
        map.put("model", Build.MODEL);
        map.put("hardware", Build.HARDWARE);
        map.put("supported_abis", Build.SUPPORTED_ABIS);
        map.put("os_version", Build.VERSION.RELEASE);
        return map;
    }

}
