package hummerx;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.didi.hummer.HummerRender;
import com.didi.hummer.adapter.navigator.NavPage;
import com.didi.hummer.adapter.navigator.impl.DefaultNavigatorAdapter;
import com.didi.hummer.context.HummerContext;
import com.didi.hummer.core.engine.JSValue;
import com.didi.hummer.render.style.HummerLayout;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class HummerDialog extends DialogFragment {
    private static final String TAG = "HummerDialog";
    private HummerLayout hmContainer;
    private HummerRender hmRender;
    private NavPage navPage;

    public void setNavPage(@NonNull NavPage page) {
        this.navPage = page;
        Bundle args = new Bundle();
        args.putSerializable(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL, page);
        setArguments(args);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (navPage == null && getArguments() != null) {
            try {
                navPage = (NavPage) getArguments().getSerializable(DefaultNavigatorAdapter.EXTRA_PAGE_MODEL);
            } catch (Exception ignored) {
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hummer_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hmContainer = view.findViewById(R.id.hummer_container);
        if (navPage != null) {
            initHummer();

            Map<String, Object> params = navPage.params;
            Object cancelable = params.get("cancelable");
            if (cancelable instanceof Boolean) {
                setCancelable((Boolean) cancelable);
            }

            Object width = params.get("width");
            Object height = params.get("height");
            Object backgroundColor = params.get("backgroundColor");
            Object radius = params.get("radius");
            Object gravity = params.get("gravity");

            DisplayMetrics displayMetrics = requireActivity().getResources().getDisplayMetrics();
            int widthPixels = displayMetrics.widthPixels, heightPixels = displayMetrics.heightPixels;
            if (width instanceof Integer) {
                widthPixels = (Integer) width;
            } else if (width instanceof Float) {
                widthPixels = (int) (displayMetrics.widthPixels * (Float) width);
            }
            if (height instanceof Integer) {
                heightPixels = (Integer) height;
            } else if (height instanceof Float) {
                heightPixels = (int) (displayMetrics.heightPixels * (Float) height);
            }

            Window window = requireDialog().getWindow();
            if (window != null) {
                window.setLayout(widthPixels, heightPixels);
                if (backgroundColor instanceof Integer) {
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setColor((Integer) backgroundColor);
                    if (radius instanceof Integer) {
                        drawable.setCornerRadius((Integer) radius);
                    } else if (radius instanceof Float) {
                        drawable.setCornerRadius((Float) radius);
                    }
                    window.setBackgroundDrawable(drawable);
                }
                if (gravity instanceof Integer) {
                    window.setGravity((Integer) gravity);
                }
            }
        } else {
            Log.e(TAG, "page is null");
        }
    }

    protected void initHummer() {
        hmRender = new HummerRender(hmContainer);
        hmRender.setJsPageInfo(navPage);
        hmRender.setRenderCallback(new HummerRender.HummerRenderCallback() {
            @Override
            public void onSucceed(HummerContext hmContext, JSValue jsPage) {
                Log.d(TAG, "onPageRenderSucceed: " + jsPage);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onPageRenderFailed: " + e);
            }
        });

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
