package com.example.demo.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.util.Predicate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

/**
 * 判断多个 TextView 的内容是否符合要求
 */
public final class InputTextManager implements TextWatcher, DefaultLifecycleObserver {

    private OnInputTextListener mListener;
    /**
     * TextView 集合
     */
    private Map<TextView, Predicate<CharSequence>> mViewMap;

    public InputTextManager(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);
    }

    public static InputTextManager with(LifecycleOwner owner) {
        return new InputTextManager(owner);
    }

    public InputTextManager setListener(OnInputTextListener listener) {
        this.mListener = listener;
        return this;
    }

    /**
     * 不能为空
     */
    public InputTextManager checkTextNotEmpty(@NonNull TextView... views) {
        checkText(text -> text != null && text.length() > 0, views);
        return this;
    }

    /**
     * 不能为空白字符
     */
    public InputTextManager checkTextNotBlank(@NonNull TextView... views) {
        checkText(text -> text != null && !text.toString().isBlank(), views);
        return this;
    }

    /**
     * 检查 TextView 满足指定条件
     */
    public InputTextManager checkText(@NonNull TextView view, @NonNull Predicate<CharSequence> predicate) {
        checkText(predicate, view);
        return this;
    }

    public void checkText(@NonNull Predicate<CharSequence> predicate, @NonNull TextView... views) {
        if (mViewMap == null) {
            mViewMap = new HashMap<>();
        }
        for (TextView view : views) {
            // 避免重复添加
            if (!mViewMap.containsKey(view)) {
                view.addTextChangedListener(this);
                mViewMap.put(view, predicate);
            }
        }
        notifyChanged();
    }

    /**
     * 移除 TextView 监听
     */
    public void removeViews(@NonNull TextView... views) {
        if (mViewMap == null || mViewMap.isEmpty()) {
            return;
        }
        for (TextView view : views) {
            view.removeTextChangedListener(this);
            mViewMap.remove(view);
        }
        notifyChanged();
    }

    public void removeAllViews() {
        if (mViewMap == null || mViewMap.isEmpty()) {
            return;
        }
        for (TextView view : mViewMap.keySet()) {
            view.removeTextChangedListener(this);
        }
        mViewMap.clear();
        mViewMap = null;
    }

    /**
     * 通知更新
     */
    public void notifyChanged() {
        if (mViewMap == null) {
            return;
        }
        for (Map.Entry<TextView, Predicate<CharSequence>> entry : mViewMap.entrySet()) {
            if (!entry.getValue().test(entry.getKey().getText())) {
                if (mListener != null) {
                    mListener.onTextEnable(false);
                }
                return;
            }
        }
        if (mListener != null) {
            mListener.onTextEnable(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        notifyChanged();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
        removeAllViews();
    }

    public interface OnInputTextListener {
        void onTextEnable(boolean enable);
    }
}