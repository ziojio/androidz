package com.example.demo.activity.databinding;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

public class State<T> extends ObservableField<T> {
    public State() {
    }

    public State(T value) {
        super(value);
    }

    public State(Observable... dependencies) {
        super(dependencies);
    }
}
