package com.example.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class AidlService extends Service {
    private LocalBinder binder;

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new LocalBinder();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binder = null;
    }

    public void showToast() {
        Toast.makeText(this, "Service中的Toast", Toast.LENGTH_SHORT).show();
    }

    // 在相同进程使用 否则使用 AIDL
    public class LocalBinder extends Binder {
        public AidlService getService() {
            return AidlService.this;
        }
    }
}
