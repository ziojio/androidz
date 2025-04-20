package com.example.demo.data.api;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface HttpService {

    @GET
    Call<ResponseBody> get(@Url String url);

    @GET
    Call<JsonElement> getJson(@Url String url);

    @GET
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);

    @POST
    Call<ResponseBody> postBody(@Url String url, @Body RequestBody body);

    @POST
    @Headers("Content-Type: application/json; charset=utf-8")
    Call<ResponseBody> postJson(@Url String url, @Body RequestBody jsonBody);


    @PUT
    Call<ResponseBody> put(@Url String url, @QueryMap Map<String, Object> map);

    @PUT
    Call<ResponseBody> putBody(@Url String url, @Body RequestBody body);

    @PUT
    @Headers("Content-Type: application/json; charset=utf-8")
    Call<ResponseBody> putJson(@Url String url, @Body RequestBody jsonBody);


    @DELETE
    Call<ResponseBody> delete(@Url String url, @QueryMap Map<String, Object> map);

    @DELETE
    Call<ResponseBody> deleteBody(@Url String url, @Body RequestBody body);

    @DELETE
    @Headers("Content-Type: application/json; charset=utf-8")
    Call<ResponseBody> deleteJson(@Url String url, @Body RequestBody jsonBody);


    @Multipart
    @POST
    Call<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part part);

    @Multipart
    @POST
    Call<ResponseBody> uploadFiles(@Url String url, @Part List<MultipartBody.Part> parts);


    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String url, @QueryMap Map<String, String> map);

}