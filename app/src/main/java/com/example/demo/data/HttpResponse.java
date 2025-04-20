package com.example.demo.data;

public class HttpResponse<T> {
    private T data;
    private int code;
    private String message;
    private HttpException error;

    public T data() {
        return data;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

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

    public static <T> HttpResponse<T> failure(HttpException e, T data, int code, String message) {
        HttpResponse<T> response = new HttpResponse<>();
        response.error = e;
        response.data = data;
        response.code = code;
        response.message = message;
        return response;
    }
}