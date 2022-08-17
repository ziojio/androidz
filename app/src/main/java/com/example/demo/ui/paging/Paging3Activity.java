package com.example.demo.ui.paging;

import android.os.Bundle;
import android.os.Handler;

import com.example.demo.databinding.ActivityPaging3Binding;
import com.example.demo.di.MainHandler;
import com.example.demo.ui.base.BaseActivity;

import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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

        PagingViewModel model = new ViewModelProvider(this).get(PagingViewModel.class);

        ItemPagingAdapter pagingAdapter = new ItemPagingAdapter();
        ExampleLoadStateAdapter footerAdapter = new ExampleLoadStateAdapter(v -> pagingAdapter.retry());
        ConcatAdapter concatAdapter = pagingAdapter.withLoadStateFooter(footerAdapter);
        binding.recyclerview.setAdapter(concatAdapter);

        // pagingAdapter.addLoadStateListener(new Function1<>() {
        //     @Override
        //     public Unit invoke(CombinedLoadStates combinedLoadStates) {
        //         postDelayed(() -> {
        //             if (combinedLoadStates.getRefresh() instanceof LoadState.NotLoading) {
        //                 binding.swipeRefreshLayout.setRefreshing(false);
        //             }
        //         }, 100);
        //         return null;
        //     }
        // });

        Handler handler = new MainHandler();
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pagingAdapter.refresh();

                handler.postDelayed(() -> {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }, 2000);
            }
        });

        Pager<Integer, ItemData> pager = new Pager<>(
                new PagingConfig(10),
                () -> new ExamplePagingSource("Page"));

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