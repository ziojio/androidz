package com.example.demo.work;

import android.content.Context;

import com.example.demo.log.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppLogUploadWork extends Worker {
    private String url;
    private File file;

    public AppLogUploadWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        File logDir = LogUtil.getLogDir(context);
        file = new File(logDir, LogUtil.getLogFileName(new Date()));
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(MediaType.parse("application/octet-stream"), file))
                    .build();
            Call call = client.newCall(request);

            Response response = call.execute();
            if (response.isSuccessful()) {
                return Result.success(new Data.Builder()
                        .putString("data", response.body().string())
                        .build());
            } else {
                return Result.failure(new Data.Builder()
                        .putString("error", response.message())
                        .build());
            }
        } catch (IOException e) {
            return Result.failure(new Data.Builder()
                    .putString("error", e.getMessage())
                    .build());
        }
    }

}
