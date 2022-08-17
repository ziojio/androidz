package com.example.demo.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


public class BindingViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
    @NonNull
    public final T binding;

    public BindingViewHolder(@NonNull T binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
