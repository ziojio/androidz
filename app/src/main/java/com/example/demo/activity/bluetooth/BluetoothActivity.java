package com.example.demo.activity.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DiffUtil;

import com.example.demo.R;
import com.example.demo.activity.BaseActivity;
import com.example.demo.adapter.BaseListAdapter;
import com.example.demo.adapter.BaseViewHolder;
import com.example.demo.databinding.ActivityBluetoothBinding;
import com.example.demo.databinding.AdapterBtdeviceBinding;
import com.example.demo.util.Timber;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;

import java.util.List;


public class BluetoothActivity extends BaseActivity {
    private ActivityBluetoothBinding binding;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {

                    Timber.d("onFound: name=" + device.getName() + ", address=" + device.getAddress());
                    BTAdapter adapter = (BTAdapter) binding.recyclerview.getAdapter();
                    if (!adapter.contains(device)) {
                        adapter.add(device);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Timber.d("onStart: ");

                BTAdapter adapter = (BTAdapter) binding.recyclerview.getAdapter();
                adapter.updateList(null);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Timber.d("onStop: ");

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(v -> finish());

        final IPermission[] bluetoothPermissions = new IPermission[]{
                PermissionLists.getBluetoothScanPermission(),
                PermissionLists.getBluetoothScanPermission(),
                PermissionLists.getAccessFineLocationPermission(),
                PermissionLists.getAccessCoarseLocationPermission(),
        };
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        binding.open.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.enable();
        });
        binding.close.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            bluetoothAdapter.disable();
        });
        binding.info.setOnClickListener(view -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Timber.d("isEnabled=" + bluetoothAdapter.isEnabled());
            Timber.d("name=" + bluetoothAdapter.getName());
            Timber.d("pairedDevices=" + bluetoothAdapter.getBondedDevices());
            // Timber.d("address=" + bluetoothAdapter.getAddress());
        });

        BTAdapter adapter = new BTAdapter();
        binding.recyclerview.setAdapter(adapter);
        binding.discover.setOnClickListener(view -> {
            adapter.submitList(null);
            if (XXPermissions.isGrantedPermissions(this, bluetoothPermissions)) {
                startDiscover();
            } else {
                XXPermissions.with(this)
                        .permissions(bluetoothPermissions)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(@NonNull List<IPermission> permissions, boolean allGranted) {
                                if (allGranted) {
                                    startDiscover();
                                }
                            }
                        });
            }
        });

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void startDiscover() {
        Timber.d("startDiscover: ");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (bluetoothAdapter.isEnabled()) {
            new Thread(() -> {
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
            }).start();
        } else {
            startActivity(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
        }
    }

    private static class BTAdapter extends BaseListAdapter<BluetoothDevice, BaseViewHolder> {
        BTAdapter() {
            super(new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull BluetoothDevice oldItem, @NonNull BluetoothDevice newItem) {
                    return oldItem.equals(newItem);
                }
            });
        }

        @NonNull
        @Override
        public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BaseViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_btdevice, parent, false));
        }

        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            BluetoothDevice device = getItem(position);
            AdapterBtdeviceBinding binding = AdapterBtdeviceBinding.bind(holder.itemView);
            binding.name.setText(device.getName() == null ? "unknown" : device.getName());
            binding.address.setText(device.getAddress());
        }
    }

}
