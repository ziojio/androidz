package com.example.demo.data;

import com.google.gson.annotations.SerializedName;

public class DataResult<T> {
    public int code;
    @SerializedName(value = "message", alternate = "msg")
    public String message;
    public T data;
}
