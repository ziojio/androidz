package com.example.demo.ui.http;

import android.os.Bundle;
import android.util.Log;

import com.example.demo.data.api.RxHttpService;
import com.example.demo.data.repository.HttpRepository;
import com.example.demo.data.response.BaseObserver;
import com.example.demo.data.response.HttpCallback;
import com.example.demo.databinding.ActivityHttpBinding;
import com.example.demo.ui.base.BaseActivity;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.Locale;

import androidx.annotation.NonNull;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpActivity extends BaseActivity {
    private ActivityHttpBinding binding;

    private RxHttpService apiService;
    private BaseObserver<JsonElement> observer;
    private Call<JsonElement> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.input.setText("https://mdn.github.io/learning-area/javascript/apis/fetching-data/can-store/products.json");
        binding.request.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                request(text);
            }
        });
        binding.rxRequest.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                rxrequest(text);
            }
        });
        binding.cancel.setOnClickListener(v -> {
            if (observer != null && !observer.isDisposed()) {
                observer.dispose();
            }
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
        });

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(client)
                .build();

        apiService = retrofit.create(RxHttpService.class);
    }

    private void request(String url) {
        call = HttpRepository.getInstance().getJson(url, new HttpCallback<>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, Response<JsonElement> response) {
                Log.d("TAG", "onResponse: " + response);
                super.onResponse(call, response);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable throwable) {
                Log.d("TAG", "onFailure: " + throwable);
                super.onFailure(call, throwable);
            }

            @Override
            public void onSuccess(JsonElement body) {
                binding.text.setText(new GsonBuilder().setPrettyPrinting().create().toJson(body));
            }

            @Override
            public void onError(int code, String message) {
                binding.text.setText(String.format(Locale.getDefault(), "code=%d, message=%s", code, message));
            }
        });
    }

    private void rxrequest(String url) {
        observer = new BaseObserver<>() {
            @Override
            public void onNext(@NonNull JsonElement jsonElement) {
                Log.d("TAG", "onNext: " + jsonElement);
                try {
                    String result = jsonElement.toString();
                    binding.text.setText(new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("TAG", "onError: " + e);
                super.onError(e);
            }

            @Override
            public void onError(int code, String message) {
                binding.text.setText(String.format(Locale.getDefault(), "code=%d, message=%s", code, message));
            }
        };
        apiService.getJson(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

}
