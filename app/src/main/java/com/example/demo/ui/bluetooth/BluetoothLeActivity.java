package com.example.demo.ui.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.demo.databinding.ActivityBluetoothLeBinding;
import com.example.demo.ui.base.BaseActivity;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import timber.log.Timber;

@SuppressLint("MissingPermission")
public class BluetoothLeActivity extends BaseActivity {
    private ActivityBluetoothLeBinding binding;
    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private boolean isScanning;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Timber.d("------------------------------------------------");
            BluetoothDevice device = result.getDevice();
            Timber.d("onScanResult name: %s, address= %s", device.getName(), device.getAddress());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Timber.d("------------------------------------------------");
            for (ScanResult result : results) {
                BluetoothDevice device = result.getDevice();
                Timber.d("onBatchScanResults name: %s, address= %s", device.getName(), device.getAddress());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Timber.d("onScanFailed " + errorCode);
        }
    };
    private String[] perms = {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }
        binding = ActivityBluetoothLeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            adapter = manager.getAdapter();
        }

        binding.open.setOnClickListener(view -> {
            adapter.enable();
        });
        binding.close.setOnClickListener(view -> {
            adapter.disable();
        });
        binding.info.setOnClickListener(view -> {
            Timber.d("name=" + adapter.getName());
            Timber.d("address=" + adapter.getAddress());
            Timber.d("isEnabled=" + adapter.isEnabled());
            ArrayList<BluetoothDevice> pairedDevices = new ArrayList<>(adapter.getBondedDevices());
            Timber.d("pairedDevices=" + pairedDevices);
        });
        binding.scan.setOnClickListener(view -> {
            if (XXPermissions.isGranted(this, perms)) {
                startScan();
            } else {
                XXPermissions.with(this)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                                if (allGranted) {
                                    startScan();
                                }
                            }
                        });
            }
        });
        binding.stopScan.setOnClickListener(view -> {
            stopScan();
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScan();
    }

    public void stopScan() {
        if (isScanning) {
            Timber.d("stopScan ");
            isScanning = false;
            adapter.getBluetoothLeScanner().stopScan(scanCallback);
        }
    }

    public void startScan() {
        Timber.d("startScan ");
        if (adapter.isEnabled()) {
            if (isScanning) {
                showToast("扫描中...");
            } else {
                isScanning = true;
                adapter.getBluetoothLeScanner().startScan(scanCallback);
            }
        } else {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

}
