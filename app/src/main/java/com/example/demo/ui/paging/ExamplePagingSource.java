package com.example.demo.ui.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

class ExamplePagingSource extends RxPagingSource<Integer, ItemData> {
    @NonNull
    private String mQuery;

    ExamplePagingSource(@NonNull String query) {
        mQuery = query;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, ItemData>> loadSingle(@NonNull LoadParams<Integer> params) {
        // Start refresh at page 1 if undefined.
        final int pageNumber = params.getKey() == null ? 1 : params.getKey();

        return Single.create(new SingleOnSubscribe<LoadResult<Integer, ItemData>>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<LoadResult<Integer, ItemData>> emitter) throws Throwable {
                        List<ItemData> itemDataList = new ArrayList<>();
                        for (int i = 0; i <= 9; i++) {
                            itemDataList.add(new ItemData(mQuery + " " + pageNumber + ": " + i));
                        }

                        Completable.timer(new Random().nextInt(3), TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .subscribe(() ->
                                        emitter.onSuccess(new LoadResult.Page<>(
                                                itemDataList,
                                                null, // Only paging forward.
                                                pageNumber + 1)));
                    }
                })
                .subscribeOn(Schedulers.io())
                .onErrorReturn(LoadResult.Error::new);
    }

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, ItemData> state) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = state.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }

        LoadResult.Page<Integer, ItemData> anchorPage = state.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }

        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }

        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }

        return null;
    }
}