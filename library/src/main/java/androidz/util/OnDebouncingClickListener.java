package androidz.util;

import android.view.View;

/**
 * 去除点击抖动
 */
public interface OnDebouncingClickListener extends View.OnClickListener {

    @Override
    default void onClick(View v) {
        if (ClickUtil.isSingleClick(v)) {
            onDebouncingClick(v);
        }
    }

    void onDebouncingClick(View v);

}