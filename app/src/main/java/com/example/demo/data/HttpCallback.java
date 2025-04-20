package com.example.demo.data;

public interface HttpCallback<T> {

    void onResult(HttpResponse<T> response);
}