package com.example.demo.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AsyncTask {

    public static void doAction(@NonNull Runnable r) {
        doAction(r, null, null);
    }

    public static void doAction(@NonNull Runnable r, @NonNull Action onComplete) {
        doAction(r, onComplete, null);
    }

    public static void doAction(@NonNull Runnable r, @Nullable Action onComplete, @Nullable Consumer<Throwable> onError) {
        Completable.fromRunnable(r)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        if (onComplete != null) {
                            try {
                                onComplete.run();
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (onError != null) {
                            try {
                                onError.accept(e);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }

    public static <T> void doAction(@NonNull Supplier<T> action, @NonNull Consumer<T> onResult) {
        doAction(action, onResult, null);
    }

    public static <T> void doAction(@NonNull Supplier<T> action, @Nullable Consumer<T> onResult, @Nullable Consumer<Throwable> onError) {
        Single.create((SingleOnSubscribe<T>) emitter -> emitter.onSuccess(action.get()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<T>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull T t) {
                        if (onResult != null) {
                            try {
                                onResult.accept(t);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (onError != null) {
                            try {
                                onError.accept(e);
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
    }
}