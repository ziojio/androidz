package com.example.demo.util;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AsyncTask {
    private static final String TAG = "AsyncTask";

    public static void doAction(@NonNull Runnable action) {
        Completable.fromRunnable(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Completable onError", e);
                    }
                });
    }

    public static void doAction(@NonNull Runnable action, @Nullable Runnable done) {
        Completable.fromRunnable(action)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        if (done != null) {
                            try {
                                done.run();
                            } catch (Throwable e) {
                                Log.e(TAG, "Completable onComplete", e);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Completable onError", e);
                    }
                });
    }

    /**
     * @param onResult null值抛异常，不会执行回调
     */
    public static <T> void doAction(@NonNull Supplier<T> action, @Nullable Consumer<T> onResult) {
        Single.create((SingleOnSubscribe<T>) emitter -> emitter.onSuccess(action.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull T t) {
                        if (onResult != null) {
                            try {
                                onResult.accept(t);
                            } catch (Throwable e) {
                                Log.e(TAG, "Single onSuccess", e);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "Single onError", e);
                    }
                });
    }

    public static <T> void doAction(@NonNull Supplier<T> action, @Nullable Consumer<T> onResult, @Nullable Consumer<Throwable> onError) {
        Single.create((SingleOnSubscribe<T>) emitter -> emitter.onSuccess(action.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull T t) {
                        if (onResult != null) {
                            try {
                                onResult.accept(t);
                            } catch (Throwable e) {
                                Log.e(TAG, "Single onSuccess", e);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (onError != null) {
                            try {
                                onError.accept(e);
                            } catch (Throwable ex) {
                                Log.e(TAG, "Single onError", ex);
                            }
                        } else {
                            Log.e(TAG, "Single onError", e);
                        }
                    }
                });
    }
}