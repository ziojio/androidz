package androidz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ziojio.androidz.R;

public class DrawableTextView extends AppCompatTextView {
    private int iconWidth;
    private int iconHeight;

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
        int iconSize = array.getDimensionPixelSize(R.styleable.DrawableTextView_iconSize, 0);
        if (iconSize == 0) {
            iconWidth = array.getDimensionPixelSize(R.styleable.DrawableTextView_iconWidth, 0);
            iconHeight = array.getDimensionPixelSize(R.styleable.DrawableTextView_iconHeight, 0);
        } else {
            iconWidth = iconHeight = iconSize;
        }
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

    public void setIconSize(int iconSize) {
        iconWidth = iconHeight = iconSize;
        updateCompoundDrawable();
    }

    public void setIconWidth(int iconWidth) {
        if (this.iconWidth != iconWidth) {
            this.iconWidth = iconWidth;
            updateCompoundDrawable();
        }
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconHeight(int iconHeight) {
        if (this.iconHeight != iconHeight) {
            this.iconHeight = iconHeight;
            updateCompoundDrawable();
        }
    }

    public int getIconHeight() {
        return iconHeight;
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
        drawable.setBounds(0, 0,
                iconWidth != 0 ? iconWidth : drawable.getIntrinsicWidth(),
                iconHeight != 0 ? iconHeight : drawable.getIntrinsicHeight());
        return drawable;
    }

}
