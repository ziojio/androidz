package com.example.demo.ui.databinding;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

/**
 * Logic is similar to superclass
 */
public class State<T> extends ObservableField<T> {
    private final boolean isDebouncing;

    public State() {
        isDebouncing = true;
    }

    public State(@Nullable T value) {
        super(value);
        isDebouncing = true;
    }

    public State(@Nullable T value, boolean debouncing) {
        super(value);
        isDebouncing = debouncing;
    }

    public void set(@Nullable T value) {
        boolean isUnChanged = get() == value;
        super.set(value);
        // super is debouncing
        if (isUnChanged && !isDebouncing) {
            notifyChange();
        }
    }

}
