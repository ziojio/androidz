package com.example.demo.ui.activity;

import android.os.Bundle;
import android.os.Looper;

import com.example.demo.databinding.ActivityRxjavaBinding;
import com.example.demo.ui.base.BaseActivity;

import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.core.FlowableSubscriber;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.MaybeEmitter;
import io.reactivex.rxjava3.core.MaybeObserver;
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
import timber.log.Timber;


public class RxJavaActivity extends BaseActivity {
    static int x = 500;
    static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityRxjavaBinding binding = ActivityRxjavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.test.setOnClickListener(v -> test());
        binding.observable.setOnClickListener(v -> observable());
        binding.flowable.setOnClickListener(v -> flowable());
        binding.callable.setOnClickListener(v -> callable());
        binding.range.setOnClickListener(v -> range());
        binding.interval.setOnClickListener(v -> interval());
        binding.maybe.setOnClickListener(v -> maybe());
        binding.single.setOnClickListener(v -> single());
        binding.complete.setOnClickListener(v -> complete());

        if (count++ % 2 == 0) {
            try {
                Thread.sleep(x);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
                        Timber.d("onComplete ");
                    }
                });
    }

    private void observable() {
        Timber.d("observable " + Thread.currentThread() + " isUiThread: " + isUiThread());
        Observable.create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d(Thread.currentThread() + " isUiThread: " + isUiThread());

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

                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext " + s);
                        Timber.d(Thread.currentThread() + " isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void flowable() {
        Flowable.create(new FlowableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d("isUiThread: " + isUiThread());

                        emitter.onNext("A");
                        Thread.sleep(200);
                        // the consumer might have cancelled the flow
                        if (emitter.isCancelled()) {
                            return;
                        }

                        emitter.onNext("B");
                        Thread.sleep(200);
                        emitter.onNext("C");
                        Thread.sleep(200);
                        emitter.onComplete();
                    }
                }, BackpressureStrategy.BUFFER)
                // .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<>() {
                    @Override
                    public void onSubscribe(@NonNull Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String s) {
                        Timber.d("onNext " + s);
                        Timber.d("isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void callable() {
        Timber.d("callable");
        Flowable.fromCallable(() -> {
                    Thread.sleep(1000); //  imitate expensive computation
                    return "Done";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .subscribe(System.out::println, Throwable::printStackTrace);
    }

    private void interval() {
        Timber.d("interval");
        Observable.intervalRange(1, 10, 1000, 10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(v -> v * v)
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Long integer) {
                        Timber.d("onNext " + integer + " isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
        Timber.d("interval End");
    }

    private void range() {
        Timber.d("range ");
        Observable.range(1, 10)
                .subscribeOn(Schedulers.computation())
                .map(v -> {
                    Timber.d("map isUiThread: " + isUiThread());
                    return v * v;
                })
                .subscribe(new Observer<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Timber.d("onNext " + integer + " isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
        Timber.d("range End");
    }

    private void maybe() {
        Maybe.create(new MaybeOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull MaybeEmitter<String> emitter) throws Throwable {
                        Timber.d("subscribe ");
                        Timber.d("isUiThread: " + isUiThread());
                        // emitter.onSuccess("A");
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new MaybeObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        Timber.d("onSuccess " + s);
                        Timber.d("isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Timber.d(t, "onError ");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete ");
                    }
                });
    }

    private void single() {
        Timber.d("single");
        Single.create(new SingleOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull SingleEmitter<String> emitter) {
                        Timber.d("subscribe ");
                        Timber.d("isUiThread: " + isUiThread());
                        emitter.onSuccess("single-item");
                    }
                })
                .subscribe(new SingleObserver<>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull String s) {
                        Timber.d("onSuccess " + s);
                        Timber.d("isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e);
                        Timber.d("isUiThread: " + isUiThread());
                    }
                });
    }

    private void complete() {
        Timber.d("execute");
        Completable.fromRunnable(() -> {
                    Timber.d("Completable fromRunnable");
                    Timber.d("isUiThread: " + isUiThread());
                    throw new IllegalStateException("Completable Error");
                })
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Done!");
                        Timber.d("isUiThread: " + isUiThread());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d(e, "onError");
                        Timber.d("isUiThread: " + isUiThread());
                    }
                });
    }

    private boolean isUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
