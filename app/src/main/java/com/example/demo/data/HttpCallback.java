package com.example.demo.data;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.UnknownHostException;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class HttpCallback<T> implements Callback {

    public abstract void onResult(HttpResponse<T> response);

    private void onResult2(HttpResponse<T> response) {
        new Handler(Looper.getMainLooper()).post(() -> onResult(response));
    }

    @Override
    public void onResponse(@NonNull Call call, Response response) {
        if (response.isSuccessful()) {
            Type genericSuperclass = getClass().getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                Type paramType = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
                String bodyString = null;
                try {
                    bodyString = response.body().string();
                } catch (IOException e) {
                    onResult2(HttpResponse.failure(new IOException("网络错误", e), response));
                }
                if (bodyString != null) {
                    if (paramType == String.class) {
                        //noinspection unchecked
                        T data = (T) bodyString;
                        onResult2(HttpResponse.success(data, response));
                    } else {
                        Gson gson = new Gson();
                        T data = null;
                        try {
                            data = gson.fromJson(bodyString, paramType);
                        } catch (Exception e) {
                            onResult2(HttpResponse.failure(new Exception("数据错误", e), response));
                        }
                        if (data != null) {
                            onResult2(HttpResponse.success(data, response));
                        }
                    }
                }
            } else {
                onResult2(HttpResponse.failure(new Exception("泛型错误"), response));
            }
        } else {
            onResult2(HttpResponse.failure(new Exception("响应错误"), response));
        }
    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        if (e instanceof ConnectException || e instanceof UnknownHostException) {
            onResult2(HttpResponse.failure(new IOException("连接错误", e)));
        } else if (e instanceof InterruptedIOException) {
            onResult2(HttpResponse.failure(new IOException("连接超时", e)));
        } else {
            if ("Canceled".equals(e.getMessage())) {
                onResult2(HttpResponse.failure(new IOException("取消请求", e)));
            } else {
                onResult2(HttpResponse.failure(new IOException("网络错误", e)));
            }
        }
    }
}