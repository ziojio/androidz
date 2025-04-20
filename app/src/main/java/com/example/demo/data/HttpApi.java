package com.example.demo.data;

import com.example.demo.UIApp;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpApi {
    private static final HttpApi sInstance = new HttpApi();

    public static HttpApi getInstance() {
        return sInstance;
    }

    private final OkHttpClient httpClient;
    private final String baseUrl = "https://localhost/";

    private HttpApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (UIApp.isDebuggable()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        httpClient = builder.build();
    }

    private HttpUrl url(String url) {
        if (url.startsWith("/")) {
            return HttpUrl.get(baseUrl + url.substring(1));
        }
        if (url.startsWith("http")) {
            return HttpUrl.get(url);
        }
        return HttpUrl.get(baseUrl + url);
    }

    public void getString(@NonNull String url, @NonNull HttpCallback<String> callback) {
        Request request = new Request.Builder().url(url(url)).build();
        httpClient.newCall(request).enqueue(callback);
    }

}
