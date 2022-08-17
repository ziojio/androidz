package com.example.demo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.example.demo.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class DrawableTextView extends AppCompatTextView {
    private int drawableSize;

    public DrawableTextView(@NonNull Context context) {
        this(context, null);
    }

    public DrawableTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public DrawableTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView, defStyleAttr, 0);
        drawableSize = array.getDimensionPixelSize(R.styleable.DrawableTextView_drawableSize, 0);
        array.recycle();

        updateCompoundDrawable();
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(limitDrawableSize(left), limitDrawableSize(top), limitDrawableSize(right), limitDrawableSize(bottom));
    }

    @Override
    public void setCompoundDrawablesRelative(@Nullable Drawable start, @Nullable Drawable top, @Nullable Drawable end, @Nullable Drawable bottom) {
        super.setCompoundDrawablesRelative(limitDrawableSize(start), limitDrawableSize(top), limitDrawableSize(end), limitDrawableSize(bottom));
    }

    public void setDrawableSize(int drawableSize) {
        this.drawableSize = drawableSize;
        updateCompoundDrawable();
    }

    public int getDrawableSize() {
        return drawableSize;
    }

    private void updateCompoundDrawable() {
        Drawable[] drawables = getCompoundDrawablesRelative();
        Drawable start = drawables[0];
        Drawable top = drawables[1];
        Drawable end = drawables[2];
        Drawable bottom = drawables[3];
        boolean hasRelativeDrawables = start != null || end != null;
        if (hasRelativeDrawables) {
            setCompoundDrawablesRelative(start, top, end, bottom);
        } else {
            drawables = getCompoundDrawables();
            start = drawables[0];
            top = drawables[1];
            end = drawables[2];
            bottom = drawables[3];
            boolean hasDrawables = start != null || top != null || end != null || bottom != null;
            if (hasDrawables) {
                setCompoundDrawables(start, top, end, bottom);
            }
        }
    }

    private Drawable limitDrawableSize(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        int width = drawableSize != 0 ? drawableSize : drawable.getIntrinsicWidth();
        int height = drawableSize != 0 ? drawableSize : drawable.getIntrinsicHeight();
        drawable.setBounds(0, 0, width, height);
        return drawable;
    }

}