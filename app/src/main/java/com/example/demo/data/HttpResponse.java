package com.example.demo.data;

import okhttp3.Response;

public class HttpResponse<T> {
    Response rawResponse;
    Exception exception;
    T data;

    public boolean isSuccess() {
        return exception == null;
    }

    public Response rawResponse() {
        return rawResponse;
    }

    public Exception exception() {
        return exception;
    }

    public T data() {
        return data;
    }

    public static <T> HttpResponse<T> success(T data, Response rawResponse) {
        HttpResponse<T> response = new HttpResponse<>();
        response.rawResponse = rawResponse;
        response.data = data;
        return response;
    }

    public static <T> HttpResponse<T> failure(Exception e) {
        HttpResponse<T> response = new HttpResponse<>();
        response.exception = e;
        return response;
    }

    public static <T> HttpResponse<T> failure(Exception e, Response rawResponse) {
        HttpResponse<T> response = new HttpResponse<>();
        response.rawResponse = rawResponse;
        response.exception = e;
        return response;
    }
}