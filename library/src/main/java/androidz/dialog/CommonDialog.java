package androidz.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

import com.ziojio.androidz.R;

import androidz.app.AppDialog;

/**
 * 有标题和取消、确认按钮的Dialog。
 *
 * <p>使用主题中的属性 dialogCornerRadius 设置弹窗圆角大小。
 *
 * @see MessageDialog 不继承，通过Builder设置显示的内容。
 */
public final class CommonDialog extends AppDialog {

    private CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_dialog);
    }

    @Override
    public void onClick(View v) {
        if (isSingleClick(v)) {
            int id = v.getId();
            if (R.id.cancel == id) {
                cancel();
            } else if (R.id.confirm == id) {
                dismiss();
            }
        }
    }

    /**
     * @noinspection unchecked
     */
    public static class Builder<B extends Builder<?>> extends AppDialog.Builder<B, CommonDialog> {
        private final ViewGroup mContainerLayout;
        private final TextView mTitleView;
        private final TextView mCancelView;
        private final TextView mConfirmView;

        public Builder(@NonNull Context context) {
            this(context, 0);
        }

        public Builder(@NonNull Context context, @StyleRes int themeId) {
            super(context, themeId);
            mContainerLayout = findViewById(R.id.dialog_container);
            mTitleView = findViewById(R.id.title);
            mCancelView = findViewById(R.id.cancel);
            mConfirmView = findViewById(R.id.confirm);
            getDialog().setOnClickListener(mCancelView, mConfirmView);
        }

        @Override
        protected CommonDialog createDialog(@NonNull Context context, int themeId) {
            return new CommonDialog(context, themeId);
        }

        public B setContentView(@LayoutRes int resId) {
            return setContentView(LayoutInflater.from(getContext()).inflate(resId, mContainerLayout, false));
        }

        public B setContentView(@NonNull View view) {
            mContainerLayout.addView(view, 1);
            return (B) this;
        }

        public B setTitle(@StringRes int resId) {
            return setTitle(getContext().getString(resId));
        }

        public B setTitle(CharSequence text) {
            if (TextUtils.isEmpty(text)) {
                mTitleView.setVisibility(View.GONE);
            } else {
                mTitleView.setText(text);
                if (mTitleView.getVisibility() != View.VISIBLE) {
                    mTitleView.setVisibility(View.VISIBLE);
                }
            }
            return (B) this;
        }

        public B setCancel(@StringRes int resId) {
            return setCancel(getContext().getString(resId));
        }

        public B setCancel(CharSequence text) {
            if (TextUtils.isEmpty(text)) {
                mCancelView.setVisibility(View.GONE);
            } else {
                mCancelView.setText(text);
                if (mCancelView.getVisibility() != View.VISIBLE) {
                    mCancelView.setVisibility(View.VISIBLE);
                }
            }
            return (B) this;
        }

        public B setConfirm(@StringRes int resId) {
            return setConfirm(getContext().getString(resId));
        }

        public B setConfirm(CharSequence text) {
            mConfirmView.setText(text);
            if (TextUtils.isEmpty(text)) {
                mConfirmView.setVisibility(View.GONE);
            } else {
                mConfirmView.setText(text);
                if (mConfirmView.getVisibility() != View.VISIBLE) {
                    mConfirmView.setVisibility(View.VISIBLE);
                }
            }
            return (B) this;
        }
    }

}