package androidz.action;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import androidz.util.ClickUtil;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * time   : 2019/09/15
 * desc   : 点击事件意图
 */
public interface ClickAction extends View.OnClickListener {

    <V extends View> V findViewById(@IdRes int id);

    default void setOnClickListener(@IdRes int... ids) {
        setOnClickListener(this, ids);
    }

    default void setOnClickListener(@Nullable View.OnClickListener listener, @IdRes int... ids) {
        for (int id : ids) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    default void setOnClickListener(View... views) {
        setOnClickListener(this, views);
    }

    default void setOnClickListener(@Nullable View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    @Override
    default void onClick(View v) {
        // 子类实现
    }

    default boolean isSingleClick(View v) {
        return ClickUtil.isSingleClick(v);
    }

    default boolean isDoubleClick(View v) {
        return ClickUtil.isDoubleClick(v);
    }

}