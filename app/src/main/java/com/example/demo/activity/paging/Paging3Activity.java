package com.example.demo.activity.paging;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import androidx.recyclerview.widget.ConcatAdapter;

import com.example.demo.activity.BaseActivity;
import com.example.demo.databinding.ActivityPaging3Binding;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import kotlinx.coroutines.CoroutineScope;


public class Paging3Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPaging3Binding binding = ActivityPaging3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        PagingViewModel model = new ViewModelProvider(this).get(PagingViewModel.class);

        ItemPagingAdapter pagingAdapter = new ItemPagingAdapter();
        ExampleLoadStateAdapter footerAdapter = new ExampleLoadStateAdapter(v -> pagingAdapter.retry());
        ConcatAdapter concatAdapter = pagingAdapter.withLoadStateFooter(footerAdapter);
        binding.recyclerview.setAdapter(concatAdapter);

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            pagingAdapter.refresh();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                binding.swipeRefreshLayout.setRefreshing(false);
            }, 2000);
        });

        Pager<Integer, ItemData> pager = new Pager<>(new PagingConfig(10), () -> new ExamplePagingSource("Page"));

        Flowable<PagingData<ItemData>> flowable = PagingRx.getFlowable(pager);
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(model);
        PagingRx.cachedIn(flowable, viewModelScope);

        Disposable dispose = flowable.subscribe(new Consumer<>() {
            @Override
            public void accept(PagingData<ItemData> pagingData) {
                pagingAdapter.submitData(getLifecycle(), pagingData);
            }
        });
    }

}