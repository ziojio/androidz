package com.example.demo.activity;

import android.content.Context;
import android.graphics.Rect;
import android.view.Gravity;
import android.widget.ImageView;

import com.example.demo.R;

import androidx.annotation.NonNull;
import razerdp.basepopup.BasePopupWindow;

/**
 * 自定义的PopupWindow
 */
public class PopupWindow extends BasePopupWindow {
    private final ImageView mIvArrow;

    public PopupWindow(Context context) {
        super(context);
        setContentView(R.layout.pop_main);
        mIvArrow = findViewById(R.id.ic_popup_arrow);
        setPopupGravity(Gravity.BOTTOM | Gravity.RIGHT);
        setPopupGravityMode(GravityMode.ALIGN_TO_ANCHOR_SIDE, GravityMode.RELATIVE_TO_ANCHOR);
    }

    @Override
    public void onPopupLayout(@NonNull Rect popupRect, @NonNull Rect anchorRect) {
        // 计算basepopup中心与anchorview中心方位
        // e.g：算出gravity == Gravity.Left，意味着Popup显示在anchorView的左侧
        int gravity = computeGravity(popupRect, anchorRect);
        // 计算垂直位置
        int vertical = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        int horizontal = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;

        if (vertical == Gravity.BOTTOM) {
            mIvArrow.setRotation(180f);
            mIvArrow.setTranslationX(96 * getContext().getResources().getDisplayMetrics().density);
        } else if (vertical == Gravity.TOP) {
            mIvArrow.setRotation(0f);
        }
    }

}
