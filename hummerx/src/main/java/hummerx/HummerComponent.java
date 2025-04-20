package hummerx;

import android.content.Context;

import com.didi.hummer.adapter.navigator.NavPage;
import com.didi.hummer.annotation.Component;
import com.didi.hummer.annotation.JsMethod;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * 自定义组件
 */
@Component("hm")
public class HummerComponent {

    @JsMethod("showDialog")
    public static void showDialog(Context context, NavPage navPage) {
        if (navPage != null && context instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            HummerDialog hummerDialog = new HummerDialog();
            String tag = null;
            if (navPage.params != null && navPage.params.containsKey("tag")) {
                tag = (String) navPage.params.get("tag");
            }
            hummerDialog.setNavPage(navPage);
            hummerDialog.show(fragmentManager, tag);
        }
    }
}
