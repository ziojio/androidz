package com.example.demo.ui.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.databinding.HolderLoadStateBinding;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleLoadStateAdapter extends LoadStateAdapter<ExampleLoadStateAdapter.LoadStateViewHolder> {
    private final View.OnClickListener mRetryCallback;

    ExampleLoadStateAdapter(@NonNull View.OnClickListener retryCallback) {
        mRetryCallback = retryCallback;
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        return new LoadStateViewHolder(HolderLoadStateBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder holder, @NonNull LoadState loadState) {
        HolderLoadStateBinding binding = HolderLoadStateBinding.bind(holder.itemView);

        if (loadState instanceof LoadState.Error) {
            LoadState.Error loadStateError = (LoadState.Error) loadState;
            binding.errorMsg.setText(loadStateError.getError().getMessage());
        }
        binding.errorMsg.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        binding.progressBar.setVisibility(loadState instanceof LoadState.Loading ? View.VISIBLE : View.GONE);
        binding.retryButton.setVisibility(loadState instanceof LoadState.Error ? View.VISIBLE : View.GONE);
        binding.retryButton.setOnClickListener(mRetryCallback);
    }

    public static class LoadStateViewHolder extends RecyclerView.ViewHolder {

        LoadStateViewHolder(@NonNull HolderLoadStateBinding binding) {
            super(binding.getRoot());
        }
    }
}