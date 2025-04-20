package com.example.demo.activity.paging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.databinding.HolderLoadStateBinding;

public class ExampleLoadStateAdapter extends LoadStateAdapter<ExampleLoadStateAdapter.LoadStateViewHolder> {
    private final View.OnClickListener mRetryCallback;

    public ExampleLoadStateAdapter(@NonNull View.OnClickListener retryCallback) {
        mRetryCallback = retryCallback;
    }

    @NonNull
    @Override
    public LoadStateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @NonNull LoadState loadState) {
        return new LoadStateViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_load_state, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LoadStateViewHolder holder, @NonNull LoadState loadState) {
        HolderLoadStateBinding binding = HolderLoadStateBinding.bind(holder.itemView);

        if (loadState instanceof LoadState.Error loadStateError) {
            binding.errorMsg.setText(loadStateError.getError().getMessage());
            binding.errorMsg.setVisibility(View.VISIBLE);
            binding.retryButton.setVisibility(View.VISIBLE);
            binding.retryButton.setOnClickListener(mRetryCallback);
        } else {
            binding.errorMsg.setVisibility(View.GONE);
            binding.retryButton.setVisibility(View.GONE);
        }
        if (loadState instanceof LoadState.Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    public static class LoadStateViewHolder extends RecyclerView.ViewHolder {
        public LoadStateViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}