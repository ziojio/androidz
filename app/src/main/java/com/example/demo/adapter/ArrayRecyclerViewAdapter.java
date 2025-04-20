package com.example.demo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public abstract class ArrayRecyclerViewAdapter<T> extends RecyclerView.Adapter<ArrayRecyclerViewAdapter.ViewHolder> {
    public final T[] data;
    public final int itemLayout;

    public ArrayRecyclerViewAdapter(@NonNull T[] data, @LayoutRes int itemLayout) {
        this.data = data;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        onBindViewHolder(holder, data[position]);
    }

    public abstract void onBindViewHolder(@NonNull ViewHolder holder, @NonNull T item);

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}