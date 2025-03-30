package hummerx;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.didi.hummer.HummerRender;
import com.didi.hummer.adapter.navigator.NavPage;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.component.input.FocusUtil;
import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;
import com.didi.hummer.render.style.HummerLayout;

/**
 * @see com.didi.hummer.HummerActivity
 */
public class HummerActivity extends AppCompatActivity {
    private static final String TAG = "HummerActivity";

    protected HummerLayout hmContainer;
    protected HummerRender hmRender;
    protected NavPage page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hummer_activity);
        hmContainer = findViewById(R.id.hummer_container);

        try {
            page = (NavPage) getIntent().getSerializableExtra(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (page != null) {
            initHummer();
            renderHummer();
        } else {
            onPageRenderFailed(new RuntimeException("page is null"));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hmRender != null) {
            hmRender.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hmRender != null) {
            hmRender.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hmRender != null) {
            hmRender.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (hmRender != null && hmRender.onBack()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        if (hmRender != null) {
            setResult(Activity.RESULT_OK, hmRender.getJsPageResultIntent());
        }
        super.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // 手指按下时，如果有键盘已弹出，则把键盘消失掉
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            FocusUtil.clearFocus(this);
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 初始化Hummer上下文，即JS运行环境
     */
    protected void initHummer() {
        hmRender = new HummerRender(hmContainer, HummerUtil.NAMESPACE_HUMMERX, HummerUtil.getDevToolsConfig());
        HummerUtil.registerComponents(hmRender.getHummerContext());
        hmRender.setJsPageInfo(page);
        hmRender.setRenderCallback(new HummerRender.HummerRenderCallback() {
            @Override
            public void onSucceed(HummerContext hmContext, JSValue jsPage) {
                onPageRenderSucceed(hmContext, jsPage);
            }

            @Override
            public void onFailed(Exception e) {
                onPageRenderFailed(e);
            }
        });
    }

    /**
     * 渲染Hummer页面
     */
    protected void renderHummer() {
        if (page == null || TextUtils.isEmpty(page.url)) {
            return;
        }

        if (page.isHttpUrl()) {
            hmRender.renderWithUrl(page.url);
        } else if (page.url.startsWith("/")) {
            hmRender.renderWithFile(page.url);
        } else {
            hmRender.renderWithAssets(page.url);
        }
    }

    /**
     * 页面渲染成功（获取页面JS对象做相应的处理）
     *
     * @param hmContext Hummer上下文
     * @param jsPage    页面对应的JS对象（null表示渲染失败）
     */
    protected void onPageRenderSucceed(@NonNull HummerContext hmContext, @NonNull JSValue jsPage) {
        Log.d(TAG, "onPageRenderSucceed: " + page.toString());
    }

    /**
     * 页面渲染失败（获取页面渲染时的异常做相应处理）
     *
     * @param e 执行js过程中抛出的异常
     */
    protected void onPageRenderFailed(@NonNull Exception e) {
        Log.e(TAG, "onPageRenderFailed", e);
    }
}