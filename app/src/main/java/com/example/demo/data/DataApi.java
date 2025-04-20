package com.example.demo.data;

import androidx.annotation.NonNull;

import com.example.demo.UIApp;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class DataApi {
    private static final String baseUrl = "https://localhost/";
    private static final OkHttpClient httpClient;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (UIApp.isDebuggable()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        httpClient = builder.build();
    }

    private static HttpUrl url(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return HttpUrl.get(url);
        }
        if (url.startsWith("/")) {
            return HttpUrl.get(baseUrl + url.substring(1));
        }
        return HttpUrl.get(baseUrl + url);
    }

    public static void getString(@NonNull String url, @NonNull HttpCallback<String> callback) {
        Request request = new Request.Builder().url(url(url)).build();
        httpClient.newCall(request).enqueue(callback);
    }

}
