package androidz.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.ziojio.androidz.R;

/**
 * 标题栏
 * <p>默认注册了返回的监听, 关闭 Activity 或 Fragment
 */
public class TitleBar extends FrameLayout {
    private TextView titleView;
    private ImageView leftView;
    private ImageView rightView;
    private TextView rightText;
    private View lineView;

    public TitleBar(Context context) {
        this(context, null, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.titlebar, this, true);

        initialize(context, attrs, defStyleAttr, defStyleRes);

        setLeftClickListener(v -> {
            if (getContext() instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) getContext();
                FragmentManager manager = activity.getSupportFragmentManager();
                if (manager.getBackStackEntryCount() > 0) {
                    manager.popBackStack();
                } else {
                    activity.finish();
                }
            } else if (getContext() instanceof Activity) {
                ((Activity) getContext()).finish();
            }
        });
    }

    private void initialize(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        titleView = findViewById(R.id.title);
        leftView = findViewById(R.id.left_icon);
        rightView = findViewById(R.id.right_icon);
        rightText = findViewById(R.id.right_text);
        lineView = findViewById(R.id.line);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, defStyleRes);
        String titleText = a.getString(R.styleable.TitleBar_title);
        Drawable leftIcon = a.getDrawable(R.styleable.TitleBar_leftIcon);
        Drawable rightIcon = a.getDrawable(R.styleable.TitleBar_rightIcon);
        String rightText = a.getString(R.styleable.TitleBar_rightText);
        boolean lineVisible = a.getBoolean(R.styleable.TitleBar_lineVisible, true);
        boolean leftVisible = a.getBoolean(R.styleable.TitleBar_leftVisible, true);
        a.recycle();

        if (titleText != null) {
            setTitle(titleText);
        }
        if (rightText != null) {
            setRightText(rightText);
        }
        if (rightIcon != null) {
            setRightView(rightIcon);
        }
        if (leftIcon != null) {
            setRightView(leftIcon);
        }
        leftView.setVisibility(leftVisible ? VISIBLE : GONE);
        lineView.setVisibility(lineVisible ? VISIBLE : GONE);
    }

    public void setLeftClickListener(OnClickListener listener) {
        this.leftView.setOnClickListener(listener);
    }

    public void setRightClickListener(OnClickListener listener) {
        this.rightView.setOnClickListener(listener);
        this.rightText.setOnClickListener(listener);
    }

    public TextView getTitleView() {
        return titleView;
    }

    public ImageView getLeftView() {
        return leftView;
    }

    public ImageView getRightView() {
        return rightView;
    }

    public TextView getRightText() {
        return rightText;
    }

    public View getLineView() {
        return lineView;
    }

    public void setTitle(CharSequence title) {
        titleView.setText(title);
    }

    public void setLeftView(Drawable drawable) {
        leftView.setImageDrawable(drawable);
    }

    public void setRightView(Drawable drawable) {
        rightView.setImageDrawable(drawable);
    }

    public void setRightText(CharSequence text) {
        rightText.setText(text);
    }
}
