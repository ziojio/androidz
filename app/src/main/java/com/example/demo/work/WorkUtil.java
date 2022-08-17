package com.example.demo.work;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import timber.log.Timber;

public class WorkUtil {

    public static void initWork(@NonNull Context context) {
        Timber.d("initWork");
        String tag = AppLogUploadWork.class.getSimpleName();
        Constraints.Builder constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            constraints.setRequiresDeviceIdle(true);
        }

        PeriodicWorkRequest workRequest = new PeriodicWorkRequest
                .Builder(AppLogUploadWork.class, 1, TimeUnit.DAYS)
                .addTag(tag)
                .setConstraints(constraints.build())
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();

        WorkManager workManager = WorkManager.getInstance(context);
        workManager.enqueueUniquePeriodicWork(tag, ExistingPeriodicWorkPolicy.UPDATE, workRequest);
    }
}
