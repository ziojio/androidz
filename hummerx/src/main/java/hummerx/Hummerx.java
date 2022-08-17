package hummerx;

import android.content.Context;
import android.util.Log;

import com.didi.hummer.Hummer;
import com.didi.hummer.HummerConfig;
import com.didi.hummer.adapter.font.impl.DefaultFontAdapter;
import com.didi.hummer.adapter.http.impl.DefaultHttpAdapter;
import com.didi.hummer.adapter.imageloader.impl.DefaultImageLoaderAdapter;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.adapter.storage.impl.DefaultStorageAdapter;
import com.didi.hummer.context.HummerContext;
import com.didi.hummer.context.HummerRegister;
import com.didi.hummer.core.exception.ExceptionCallback;
import com.didi.hummer.register.HummerRegister$$hummerx;

import hummerx.adapter.MyIntentCreator;


public class Hummerx {

    public static void initHummer(Context context) {
        HummerConfig config = new HummerConfig.Builder()
                // 自定义namespace（用于业务线隔离，需和Hummer容器中的namespace配合使用，可选）
                .setNamespace("Hummerx")
                // JS异常回调（可选）
                .setExceptionCallback(new ExceptionCallback() {
                    @Override
                    public void onException(Exception e) {
                        if (e != null) {
                            Log.e("Hummerx", "JSException", e);
                        }
                    }
                })
                .setSupportRTL(false)
                // 字体文件Assets目录设置（可选）
                .setFontAdapter(new DefaultFontAdapter("fonts"))
                // 自定义路由（可在这里指定自定义Hummer容器，可选）
                .setNavigatorAdapter(new DefaultNavigatorAdapter(new MyIntentCreator()))
                // 自定义图片库（可选）
                .setImageLoaderAdapter(new DefaultImageLoaderAdapter())
                // 自定义网络库（可选）
                .setHttpAdapter(new DefaultHttpAdapter())
                // 自定义定位（可选）
                // .setLocationAdapter(new DefaultLocationAdapter())
                // 自定义持久化存储（可选）
                .setStorageAdapter(new DefaultStorageAdapter())
                .addHummerRegister(new HummerRegister() {
                    @Override
                    public void register(HummerContext hummerContext) {
                        HummerRegister$$hummerx.init(hummerContext);
                    }
                })
                .builder();
        Hummer.init(context, config);
    }


    public static void registerComponents(HummerContext hmContext) {
        HummerRegister$$hummerx.init(hmContext);
    }
}
