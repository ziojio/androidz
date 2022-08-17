package hummerx.component;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.didi.hummer.annotation.Component;
import com.didi.hummer.annotation.JsMethod;

import java.util.HashMap;
import java.util.Map;

@Component("Hummerx")
public class HMBridge {

    @JsMethod("getAppInfo")
    public Map<String, Object> getAppInfo(Context context) {
        Map<String, Object> map = new HashMap<>();
        PackageManager pm = context.getPackageManager();
        ApplicationInfo info = context.getApplicationInfo();
        map.put("appName", info.loadLabel(pm));
        map.put("debug", (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            map.put("versionCode", packageInfo.versionCode);
            map.put("versionName", packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @JsMethod("toast")
    public void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration >= 2000 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }

    @JsMethod("log")
    public void log(int priority, String tag, String msg) {
        if (tag == null || msg == null) {
            return;
        }
        if (priority < Log.VERBOSE || priority > Log.ASSERT) {
            return;
        }
        Log.println(priority, tag, msg);
    }

}
