package com.example.demo.activity;

import android.os.Bundle;

import com.example.demo.data.HttpApi;
import com.example.demo.data.HttpCallback;
import com.example.demo.data.HttpResponse;
import com.example.demo.data.RestApi;
import com.example.demo.databinding.ActivityHttpBinding;
import com.example.demo.util.Timber;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpActivity extends BaseActivity {
    private static final String TAG = "HttpActivity";

    private ActivityHttpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHttpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        binding.input.setText("https://mdn.github.io/learning-area/javascript/apis/fetching-data/can-store/products.json");
        binding.request.setOnClickListener(v -> {
            String text = binding.input.getText().toString();
            if (text.isBlank()) {
                showToast("text is Blank");
            } else {
                request(text);
            }
        });
        binding.cancel.setOnClickListener(v -> {

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
        RestApi restApi = retrofit.create(RestApi.class);
    }

    private void request(String url) {
        HttpApi.getInstance().getString(url, new HttpCallback<>() {
            @Override
            public void onResult(HttpResponse<String> response) {
                Timber.d(response + "");
                if (response.isSuccess()) {
                    binding.text.setText(response.data());
                } else {
                    binding.text.setText(response.error().getMessage());
                }
            }
        });
    }
}
