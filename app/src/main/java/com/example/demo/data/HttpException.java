package com.example.demo.data;

public class HttpException extends Exception {
    private int code;

    public HttpException(String message) {
        super(message);
    }

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "HttpException{code=" + code + ", message='" + getMessage() + "', cause=" + getCause() + "}";
    }
}