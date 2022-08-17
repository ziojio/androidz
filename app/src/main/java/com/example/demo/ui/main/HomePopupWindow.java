package com.example.demo.ui.main;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.demo.R;

import androidx.annotation.NonNull;
import razerdp.basepopup.BasePopupWindow;

/**
 * 自定义的PopupWindow
 */
public class HomePopupWindow extends BasePopupWindow {
    private ImageView mIvArrow;

    public HomePopupWindow(Context context) {
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
        boolean verticalCenter = false;
        boolean vert = (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM;
        Log.d("TAG", "BOTTOM: " + vert);
        vert = (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP;
        Log.d("TAG", "TOP: " + vert);
        vert = (gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.CENTER_VERTICAL;
        Log.d("TAG", "CENTER_VERTICAL: " + vert);
        vert = (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.LEFT;
        Log.d("TAG", "LEFT: " + vert);
        vert = (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.RIGHT;
        Log.d("TAG", "RIGHT: " + vert);
        vert = (gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL;
        Log.d("TAG", "CENTER_HORIZONTAL: " + vert);
        // 计算垂直位置
        int vertical = gravity & Gravity.VERTICAL_GRAVITY_MASK;
        int horizontal = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;

        if (vertical == Gravity.TOP) {
            mIvArrow.setVisibility(View.VISIBLE);
            // 设置箭头水平位置为相对于basepopup居中
            mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()) >> 1);
            // 设置箭头垂直位置为相对于basepopup底部
            mIvArrow.setTranslationY(popupRect.height() - mIvArrow.getHeight());
            // 设置旋转角度0度（即箭头朝下，具体根据您的初始切图而定）
            mIvArrow.setRotation(0f);
        } else if (vertical == Gravity.BOTTOM) {
            mIvArrow.setVisibility(View.VISIBLE);
            mIvArrow.setTranslationY(0);
            mIvArrow.setRotation(180f);

            if (horizontal == Gravity.LEFT) {
                mIvArrow.setTranslationX(popupRect.width() - mIvArrow.getWidth() - mIvArrow.getWidth() / 2f);
            } else if (horizontal == Gravity.RIGHT) {
                mIvArrow.setTranslationX(mIvArrow.getWidth());
            } else if (horizontal == Gravity.CENTER_HORIZONTAL) {
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()) >> 1);
            }
        } else if (vertical == Gravity.CENTER_VERTICAL) {
            verticalCenter = true;
        }
        // switch (horizontal) {
        //     case Gravity.LEFT:
        //         mIvArrow.setVisibility(View.VISIBLE);
        //         mIvArrow.setTranslationX(popupRect.width() - mIvArrow.getWidth());
        //         mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()) >> 1);
        //         mIvArrow.setRotation(270f);
        //         break;
        //     case Gravity.RIGHT:
        //         mIvArrow.setVisibility(View.VISIBLE);
        //         mIvArrow.setTranslationX(0);
        //         mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()) >> 1);
        //         mIvArrow.setRotation(90f);
        //         break;
        //     case Gravity.CENTER_HORIZONTAL:
        //         //如果basepopup与anchorview中心对齐，则隐藏箭头
        //         mIvArrow.setVisibility(verticalCenter ? View.INVISIBLE : View.VISIBLE);
        //         break;
        // }
    }

}
