package com.example.demo.ui.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.demo.R;
import com.example.demo.databinding.ActivityBluetoothBinding;
import com.example.demo.databinding.AdapterBtdeviceBinding;
import com.example.demo.ui.adapter.BaseListAdapter;
import com.example.demo.ui.adapter.BaseViewHolder;
import com.example.demo.ui.base.BaseActivity;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import timber.log.Timber;

@SuppressLint("MissingPermission")
public class BluetoothActivity extends BaseActivity {
    private ActivityBluetoothBinding binding;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String[] perms = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Timber.d("onFound: name=" + device.getName() + ", address=" + device.getAddress());

                BTAdapter adapter = (BTAdapter) binding.recyclerview.getAdapter();
                if (!adapter.contains(device)) {
                    adapter.add(device);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
            };
        }
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        binding.open.setOnClickListener(view -> {
            bluetoothAdapter.enable();
        });
        binding.close.setOnClickListener(view -> {
            bluetoothAdapter.disable();
        });
        binding.info.setOnClickListener(view -> {
            Timber.d("name=" + bluetoothAdapter.getName());
            Timber.d("address=" + bluetoothAdapter.getAddress());
            Timber.d("isEnabled=" + bluetoothAdapter.isEnabled());
            Timber.d("pairedDevices=" + bluetoothAdapter.getBondedDevices());
        });

        BTAdapter adapter = new BTAdapter();
        binding.recyclerview.setAdapter(adapter);
        binding.discover.setOnClickListener(view -> {
            adapter.submitList(null);
            if (XXPermissions.isGranted(this, perms)) {
                startDiscover();
            } else {
                XXPermissions.with(this)
                        .permission(perms)
                        .request(new OnPermissionCallback() {
                            @Override
                            public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
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
        if (bluetoothAdapter.isEnabled()) {
            new Thread() {
                @Override
                public void run() {
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    bluetoothAdapter.startDiscovery();
                }
            }.start();
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

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
            BluetoothDevice device = getItem(position);
            AdapterBtdeviceBinding binding = AdapterBtdeviceBinding.bind(holder.itemView);
            binding.name.setText(device.getName() == null ? "unknown" : device.getName());
            binding.address.setText(device.getAddress());
        }
    }

}
