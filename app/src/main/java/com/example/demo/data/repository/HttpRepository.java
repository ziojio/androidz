package com.example.demo.data.repository;

import com.example.demo.UIApp;
import com.example.demo.data.api.HttpService;
import com.example.demo.data.response.HttpCallback;
import com.google.gson.JsonElement;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRepository {
    private static final HttpRepository sHttpRepository = new HttpRepository();

    private final HttpService httpService;

    private HttpRepository() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (UIApp.isDebuggable()) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpRepository getInstance() {
        return sHttpRepository;
    }

    public void getString(@NonNull String url, @NonNull HttpCallback<String> callback) {
        httpService.get(url).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String result;
                    try {
                        ResponseBody body = response.body();
                        result = body != null ? body.string() : "";
                    } catch (Exception e) {
                        callback.onFailure(call, e);
                        return;
                    }
                    callback.onSuccess(result);
                } else {
                    callback.onFailure(call, new HttpException(response));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                callback.onFailure(call, throwable);
            }
        });
    }

    public Call<JsonElement> getJson(@NonNull String url, @NonNull HttpCallback<JsonElement> callback) {
        Call<JsonElement> call = httpService.getJson(url);
        call.enqueue(callback);
        return call;
    }

}
