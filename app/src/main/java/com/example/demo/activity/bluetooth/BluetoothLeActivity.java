package com.example.demo.activity.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.example.demo.activity.BaseActivity;
import com.example.demo.databinding.ActivityBluetoothLeBinding;
import com.example.demo.util.Timber;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("MissingPermission")
public class BluetoothLeActivity extends BaseActivity {
    private ActivityBluetoothLeBinding binding;
    private BluetoothManager manager;
    private BluetoothAdapter adapter;
    private boolean isScanning;
    private final ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            Timber.d("------------------------------------------------");
            Timber.d("onScanResult name: " + device.getName() + ", address: " + device.getAddress());
            Timber.d("------------------------------------------------");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult result : results) {
                Timber.d("------------------------------------------------");
                BluetoothDevice device = result.getDevice();
                Timber.d("onBatchScanResults name: " + device.getName() + ", address: " + device.getAddress());
            }
            Timber.d("------------------------------------------------");
        }

        @Override
        public void onScanFailed(int errorCode) {
            Timber.d("onScanFailed " + errorCode);
        }
    };
    private IPermission[] bluetoothPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothPermissions = new IPermission[]{
                PermissionLists.getBluetoothScanPermission(),
                PermissionLists.getBluetoothScanPermission(),
                PermissionLists.getAccessFineLocationPermission(),
                PermissionLists.getAccessCoarseLocationPermission(),
        };
        binding = ActivityBluetoothLeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

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
            if (XXPermissions.isGrantedPermissions(this, bluetoothPermissions)) {
                startScan();
            } else {
                XXPermissions.with(this)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(@NonNull List<IPermission> permissions, boolean allGranted) {
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
