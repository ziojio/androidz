package com.example.demo.ui.paging;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.demo.databinding.ItemDemoBinding;

import androidx.annotation.NonNull;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


class ItemPagingAdapter extends PagingDataAdapter<ItemData, ItemPagingAdapter.ViewHolder> {
    ItemPagingAdapter() {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull ItemData oldItem, @NonNull ItemData newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull ItemData oldItem, @NonNull ItemData newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDemoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDemoBinding binding = ItemDemoBinding.bind(holder.itemView);
        ItemData item = getItem(position);

        binding.text.setText(item.name);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemDemoBinding binding;

        public ViewHolder(@NonNull ItemDemoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}