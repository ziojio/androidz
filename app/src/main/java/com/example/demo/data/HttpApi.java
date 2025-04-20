package com.example.demo.data;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.example.demo.UIApp;
import com.example.demo.util.Timber;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public final class HttpApi {
    private static final HttpApi HTTP_API = new HttpApi();

    public static HttpApi getInstance() {
        return HTTP_API;
    }

    private String baseUrl = "https://localhost/";
    private OkHttpClient httpClient;
    private Executor callbackExecutor;

    private HttpApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (UIApp.isDebuggable()) {
            builder.addInterceptor(new HttpLoggingInterceptor(Timber::d).setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        httpClient = builder.build();
        callbackExecutor = new Handler(Looper.getMainLooper())::post;
    }

    private HttpUrl mainUrl(@NonNull String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return HttpUrl.get(url);
        }
        if (url.startsWith("/")) {
            return HttpUrl.get(baseUrl + url.substring(1));
        }
        return HttpUrl.get(baseUrl + url);
    }

    private <T> Callback wrapperCallback(@NonNull HttpCallback<T> callback) {
        return new Callback() {
            private void onResult(HttpResponse<T> response) {
                callbackExecutor.execute(() -> callback.onResult(response));
            }

            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    String bodyString;
                    try {
                        bodyString = response.body().string();
                    } catch (IOException e) {
                        onResult(HttpResponse.failure(new HttpException("网络错误", e), response.code(), response.message()));
                        return;
                    }
                    Type genericInterfaces = callback.getClass().getGenericInterfaces()[0];
                    Type paramType;
                    if (genericInterfaces instanceof ParameterizedType) {
                        paramType = ((ParameterizedType) genericInterfaces).getActualTypeArguments()[0];
                    } else {
                        paramType = String.class;
                    }
                    if (paramType == String.class) {
                        T data = (T) bodyString;
                        onResult(HttpResponse.success(data, response.code(), response.message()));
                        return;
                    }
                    Gson gson = new Gson();
                    T data;
                    try {
                        data = gson.fromJson(bodyString, paramType);
                    } catch (Exception e) {
                        onResult(HttpResponse.failure(new HttpException("数据错误", e), response.code(), response.message()));
                        return;
                    }
                    onResult(HttpResponse.success(data, response.code(), response.message()));
                } else {
                    onResult(HttpResponse.failure(new HttpException("响应错误"), response.code(), response.message()));
                }
            }

            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e instanceof ConnectException || e instanceof UnknownHostException) {
                    onResult(HttpResponse.failure(new HttpException("连接错误", e)));
                } else if (e instanceof InterruptedIOException) {
                    onResult(HttpResponse.failure(new HttpException("连接超时", e)));
                } else {
                    if ("Canceled".equals(e.getMessage())) {
                        onResult(HttpResponse.failure(new HttpException("取消请求", e)));
                    } else {
                        onResult(HttpResponse.failure(new HttpException("网络错误", e)));
                    }
                }
            }
        };
    }

    public void cancel(Object tag) {
        Dispatcher dispatcher = httpClient.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (Objects.equals(call.request().tag(), tag)) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (Objects.equals(call.request().tag(), tag)) {
                call.cancel();
            }
        }
    }

    public void getString(@NonNull String url, @NonNull HttpCallback<String> callback) {
        Request request = new Request.Builder().url(mainUrl(url)).build();
        httpClient.newCall(request).enqueue(wrapperCallback(callback));
    }
}
