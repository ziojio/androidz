package com.example.demo.ui.databinding.bindingadapter;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewBindingAdapter {

    @BindingAdapter(value = {"adapter"}, requireAll = false)
    public static void setAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter(value = {"submitList"}, requireAll = false)
    public static void submitList(RecyclerView recyclerView, List list) {
        if (recyclerView.getAdapter() != null) {
            ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
            adapter.submitList(list);
        }
    }

    @BindingAdapter(value = {"autoScrollToTopWhenInsert", "autoScrollToBottomWhenInsert"}, requireAll = false)
    public static void bindList(RecyclerView recyclerView,
                                boolean autoScrollToTopWhenInsert,
                                boolean autoScrollToBottomWhenInsert) {
        if (recyclerView.getAdapter() != null) {
            recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    if (autoScrollToTopWhenInsert) {
                        recyclerView.scrollToPosition(0);
                    } else if (autoScrollToBottomWhenInsert) {
                        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount());
                    }
                }
            });
        }
    }
}