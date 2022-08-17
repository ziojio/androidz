package com.example.demo.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseListAdapter<T, VH extends RecyclerView.ViewHolder> extends ListAdapter<T, VH> {

    public BaseListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }

    public void add(T model) {
        List<T> list = new ArrayList<>(getCurrentList());
        list.add(model);
        submitList(list);
    }

    public void remove(T model) {
        if (getCurrentList().contains(model)) {
            List<T> list = new ArrayList<>(getCurrentList());
            list.remove(model);
            submitList(list);
        }
    }

    public boolean contains(T model) {
        return getCurrentList().contains(model);
    }

    /**
     * 创建新的列表用于更新
     *
     * @see #submitList(List)
     */
    public void updateList(@Nullable List<T> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }
}