package com.example.demo.data;

// public abstract class HttpCallback<T> {
//
//     public abstract void onResult(HttpResponse<T> response);
// }

public interface HttpCallback<T> {

    void onResult(HttpResponse<T> response);
}