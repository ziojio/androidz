package com.example.demo.ui.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected final List<T> mList;

    public BaseAdapter() {
        this.mList = new ArrayList<>();
    }

    public BaseAdapter(@NonNull List<T> list) {
        this.mList = list;
    }

    public BaseAdapter(@NonNull T[] t) {
        this.mList = Arrays.asList(t);
    }

    public void add(T t) {
        mList.add(t);
        notifyItemInserted(mList.size() - 1);
    }

    public void remove(T t) {
        int index = mList.indexOf(t);
        if (index >= 0) {
            mList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public boolean contains(T t) {
        return mList.contains(t);
    }

    protected T getItem(int position) {
        return mList.get(position);
    }

    public void updateList(@Nullable List<T> list) {
        if (list == mList) return;
        int count = mList.size();
        if (count != 0) {
            mList.clear();
            notifyItemRangeRemoved(0, count);
        }
        if (list != null) {
            mList.addAll(list);
            notifyItemRangeInserted(0, mList.size());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}