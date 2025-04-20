package com.example.demo.data;

import com.google.errorprone.annotations.CanIgnoreReturnValue;

public class HttpResponse<T> {
    T data;
    int code;
    String message;
    HttpException error;

    public T data() {
        return data;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    @CanIgnoreReturnValue
    public HttpException error() {
        return error;
    }

    public boolean isSuccess() {
        return error == null;
    }

    @Override
    public String toString() {
        return "HttpResponse{data=" + data
                + ", code=" + code
                + ", message=" + message
                + ", error=" + error
                + '}';
    }

    public static <T> HttpResponse<T> success(T data, int code, String message) {
        HttpResponse<T> response = new HttpResponse<>();
        response.data = data;
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> HttpResponse<T> failure(HttpException e) {
        HttpResponse<T> response = new HttpResponse<>();
        response.error = e;
        return response;
    }

    public static <T> HttpResponse<T> failure(HttpException e, int code, String message) {
        HttpResponse<T> response = new HttpResponse<>();
        response.error = e;
        response.code = code;
        response.message = message;
        return response;
    }
}