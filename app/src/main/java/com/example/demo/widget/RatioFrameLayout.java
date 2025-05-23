package com.example.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo.R;

/**
 * author : Android 轮子哥
 * github : https://github.com/getActivity/AndroidProject
 * desc   : 按照比例显示的 FrameLayout
 */
public class RatioFrameLayout extends FrameLayout {
    private float widthRatio;
    private float heightRatio;

    public RatioFrameLayout(Context context) {
        this(context, null, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RatioFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioFrameLayout);
        String sizeRatio = array.getString(R.styleable.RatioFrameLayout_ratio);
        array.recycle();

        if (!TextUtils.isEmpty(sizeRatio)) {
            String[] arrays = sizeRatio.split(":");
            switch (arrays.length) {
                case 1:
                    widthRatio = Float.parseFloat(arrays[0]);
                    heightRatio = 1;
                    break;
                case 2:
                    widthRatio = Float.parseFloat(arrays[0]);
                    heightRatio = Float.parseFloat(arrays[1]);
                    break;
                default:
                    throw new IllegalArgumentException("sizeRatio(w:h): " + sizeRatio);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (widthRatio > 0 && heightRatio > 0) {

            float sizeRatio = widthRatio / heightRatio;

            ViewGroup.LayoutParams layoutParams = getLayoutParams();

            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

            // 一般情况下 LayoutParams.WRAP_CONTENT 对应着 MeasureSpec.AT_MOST（自适应），但是由于我们在代码中强制修改了测量模式为 MeasureSpec.EXACTLY（固定值）
            // 这样会有可能重新触发一次 onMeasure 方法，这个时候传入测量模式的就不是 MeasureSpec.AT_MOST（自适应） 模式，而是 MeasureSpec.EXACTLY（固定值）模式
            // 所以我们要进行双重判断，首先判断 LayoutParams，再判断测量模式，这样就能避免因为修改了测量模式触发对宽高的重新计算，最终导致计算结果和上次计算的不同
            if (layoutParams.width != LayoutParams.WRAP_CONTENT && layoutParams.height != LayoutParams.WRAP_CONTENT
                    && widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                // 如果当前宽度和高度都是写死的
                if (widthSpecSize / sizeRatio <= heightSpecSize) {
                    // 如果宽度经过比例换算不超过原有的高度
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSpecSize / sizeRatio), MeasureSpec.EXACTLY);
                } else if (heightSpecSize * sizeRatio <= widthSpecSize) {
                    // 如果高度经过比例换算不超过原有的宽度
                    widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (heightSpecSize * sizeRatio), MeasureSpec.EXACTLY);
                }
            } else if (layoutParams.width != LayoutParams.WRAP_CONTENT && widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode != MeasureSpec.EXACTLY) {
                // 如果当前宽度是写死的，但是高度不写死
                heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSpecSize / sizeRatio), MeasureSpec.EXACTLY);
            } else if (layoutParams.height != LayoutParams.WRAP_CONTENT && heightSpecMode == MeasureSpec.EXACTLY && widthSpecMode != MeasureSpec.EXACTLY) {
                // 如果当前高度是写死的，但是宽度不写死
                widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (heightSpecSize * sizeRatio), MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}