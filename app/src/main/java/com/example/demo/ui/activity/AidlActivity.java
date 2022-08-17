package com.example.demo.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.demo.databinding.ActivityAidlBinding;
import com.example.demo.service.AidlService;
import com.example.demo.ui.base.BaseActivity;

public class AidlActivity extends BaseActivity {
    private ActivityAidlBinding binding;
    private AidlService aidlService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // by AIDL
            // aidlService = (AidlService) service;
            aidlService = ((AidlService.LocalBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            aidlService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAidlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(this, AidlService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

        binding.execFunction.setOnClickListener(v -> {
            if (aidlService != null) {
                aidlService.showToast();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
