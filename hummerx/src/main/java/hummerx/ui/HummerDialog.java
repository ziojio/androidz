package hummerx.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.didi.hummer.HummerRender;
import com.didi.hummer.adapter.navigator.NavPage;
import com.didi.hummer.adapter.navigator.impl.ActivityStackManager;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.annotation.Component;
import com.didi.hummer.annotation.JsMethod;
import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;
import com.didi.hummer.lifecycle.IFullLifeCycle;
import com.didi.hummer.render.component.view.HMBase;
import com.didi.hummer.render.style.HummerLayout;

import hummerx.R;

@Component("HMDialog")
public class HummerDialog extends DialogFragment implements IFullLifeCycle {
    private static final String TAG = "HummerDialog";

    protected NavPage navPage;
    protected HummerLayout hmContainer;
    protected HummerRender hmRender;
    protected View mView;
    protected float width;
    protected float height;
    protected int bgcolor;
    protected int radius;
    protected int gravity;

    public HummerDialog() {
        Bundle args = new Bundle();
        setArguments(args);
    }

    @JsMethod("setPage")
    public void setPage(NavPage page) {
        if (page != null) {
            this.navPage = page;
            getArguments().putSerializable(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL, page);
        }
    }

    @JsMethod("setBackground")
    public void setBackground(int bgcolor, int radius) {
        this.bgcolor = bgcolor;
        getArguments().putInt("bgcolor", bgcolor);
        this.radius = radius;
        getArguments().putInt("radius", radius);
    }

    @JsMethod("setWidthHeight")
    public void setLayoutParams(float width, float height) {
        this.width = width;
        getArguments().putFloat("width", width);
        this.height = height;
        getArguments().putFloat("height", height);
    }

    @JsMethod("setGravity")
    public void setGravity(int gravity) {
        this.gravity = gravity;
        getArguments().putInt("gravity", gravity);
    }

    @JsMethod("setCancelable")
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }

    @JsMethod("setView")
    public void setView(HMBase view) {
        mView = view.getView();
    }

    @JsMethod("renderView")
    public void renderView(HMBase view) {
        mView = view.getView();
        hmContainer.removeAllViews();
        hmContainer.addView(mView);
    }

    @JsMethod("show")
    public void show(String tag) {
        Activity activity = ActivityStackManager.getInstance().getTopActivity();
        if (activity instanceof FragmentActivity) {
            super.show(((FragmentActivity) activity).getSupportFragmentManager(), tag);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navPage = getPageInfo();
        width = getArguments().getFloat("width", -1);
        height = getArguments().getFloat("height", -1);
        bgcolor = getArguments().getInt("bgcolor");
        radius = getArguments().getInt("radius");
        gravity = getArguments().getInt("gravity");
    }

    private NavPage getPageInfo() {
        NavPage page = null;
        if (getArguments() != null) {
            try {
                page = (NavPage) getArguments().getSerializable(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL);
            } catch (Exception ignored) {
            }
        }
        return page;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hummer_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hmContainer = view.findViewById(R.id.hummer_layout);
        if (navPage != null) {
            initHummer();
            renderHummer();
        } else if (mView != null) {
            initHummer();
            hmContainer.removeAllViews();
            hmContainer.addView(view);
        } else {
            onPageRenderFailed(new RuntimeException("page is null"));
        }
        setWindowStyle(requireDialog());
    }

    protected void setWindowStyle(Dialog dialog) {
        Log.d(TAG, String.format("width=%s, height=%s", width, height));
        if (width != 0) {
            if (width > 0 && width < 1) {
                width = requireContext().getResources().getDisplayMetrics().widthPixels * width;
            }
        }
        if (height != 0) {
            if (height > 0 && height < 1) {
                height = requireContext().getResources().getDisplayMetrics().heightPixels * height;
            }
        }
        dialog.getWindow().setLayout((int) width, (int) height);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
    }

    /**
     * 初始化Hummer上下文，即JS运行环境
     */
    protected void initHummer() {
        hmRender = new HummerRender(hmContainer);
        if (navPage != null) {
            hmRender.setJsPageInfo(navPage);
        }
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

    protected void renderHummer() {
        if (navPage == null || TextUtils.isEmpty(navPage.url)) {
            return;
        }

        if (navPage.isHttpUrl()) {
            hmRender.renderWithUrl(navPage.url);
        } else if (navPage.url.startsWith("/")) {
            hmRender.renderWithFile(navPage.url);
        } else {
            hmRender.renderWithAssets(navPage.url);
        }
    }

    /**
     * 页面渲染成功的回调
     */
    protected void onPageRenderSucceed(@NonNull HummerContext hmContext, @NonNull JSValue jsPage) {

    }

    /**
     * 页面渲染失败的回调
     */
    protected void onPageRenderFailed(@NonNull Exception e) {

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (hmRender != null) {
            hmRender.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hmRender != null) {
            hmRender.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (hmRender != null) {
            hmRender.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (hmRender != null) {
            hmRender.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (hmRender != null) {
            hmRender.onDestroy();
        }
    }

}
