package com.example.demo.data.api;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

/**
 * 实体类或参数化类型：观测器行为稍有不同
 *
 * @see retrofit2.Response<>
 * @see retrofit2.adapter.rxjava3.Result<>
 * @see retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
 */
public interface RxHttpService {

    @GET
    Observable<ResponseBody> get(@Url String url);

    @GET
    Observable<JsonElement> getJson(@Url String url);

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, String> map);


    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, String> map);

    @POST
    Observable<ResponseBody> postBody(@Url String url, @Body RequestBody body);

    @POST
    @Headers("Content-Type: application/json; charset=utf-8")
    Observable<ResponseBody> postJson(@Url String url, @Body RequestBody jsonBody);


    @PUT
    Observable<ResponseBody> put(@Url String url, @QueryMap Map<String, Object> map);

    @PUT
    Observable<ResponseBody> putBody(@Url String url, @Body RequestBody body);

    @PUT
    @Headers("Content-Type: application/json; charset=utf-8")
    Observable<ResponseBody> putJson(@Url String url, @Body RequestBody jsonBody);


    @DELETE
    Observable<ResponseBody> delete(@Url String url, @QueryMap Map<String, Object> map);

    @DELETE
    Observable<ResponseBody> deleteBody(@Url String url, @Body RequestBody body);

    @DELETE
    @Headers("Content-Type: application/json; charset=utf-8")
    Observable<ResponseBody> deleteJson(@Url String url, @Body RequestBody jsonBody);


    @Multipart
    @POST
    Observable<ResponseBody> uploadFile(@Url String url, @Part MultipartBody.Part part);

    @Multipart
    @POST
    Observable<ResponseBody> uploadFiles(@Url String url, @Part List<MultipartBody.Part> parts);


    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url, @QueryMap Map<String, String> map);

}