package com.example.demo.activity;

import android.os.Bundle;

import com.example.demo.databinding.ActivityRxjavaBinding;
import com.example.demo.util.Timber;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeOnSubscribe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RxJavaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRxjavaBinding binding = ActivityRxjavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.test.setOnClickListener(v -> test());
        binding.observable.setOnClickListener(v -> observable());
        binding.complete.setOnClickListener(v -> complete());
        binding.single.setOnClickListener(v -> single());
    }

    private void test() {
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        for (int i = 1; i <= 10; i++) {
                            emitter.onNext(i + "");
                            Thread.sleep(20);
                        }
                        emitter.onComplete();
                    }
                })
                .concatMap(new Function<String, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(String s) throws Throwable {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                                emitter.onNext("a" + s);
                                emitter.onComplete();
                            }
                        }).subscribeOn(Schedulers.io());
                    }
                })
                .mergeWith(Maybe.create(new MaybeOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                        Timber.d("mergeWith");
                        emitter.onSuccess("c1");
                        Thread.sleep(20);
                    }
                }))
                .concatWith(Maybe.create(new MaybeOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                        Timber.d("concatWith");
                        emitter.onSuccess("b");
                    }
                }))
                .mergeWith(Maybe.create(new MaybeOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                        Timber.d("mergeWith");
                        emitter.onSuccess("c2");
                    }
                }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        Timber.d("onNext " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("onError " + e);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete");
                    }
                });
    }

    private void observable() {
        Timber.d("observable " + Thread.currentThread());
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe " + Thread.currentThread());

                        emitter.onNext("A");
                        Thread.sleep(200);
                        emitter.onNext("B");
                        Thread.sleep(200);
                        emitter.onNext("C");
                        Thread.sleep(200);
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Timber.d("onSubscribe " + Thread.currentThread());
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext " + Thread.currentThread() + " " + s);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.e("onError " + t);
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete " + Thread.currentThread());
                    }
                });
    }

    private void single() {
        Timber.d("single");
        Single.create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<String> emitter) {
                        emitter.onSuccess(null);
                    }
                })
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        Timber.d("onSuccess " + s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("onError " + e);
                    }
                });
    }

    private void complete() {
        Timber.d("execute");
        Completable.fromRunnable(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.e("onError " + e);
                    }
                });
    }
}
