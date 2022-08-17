package com.example.demo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.demo.databinding.ActivityDeviceBinding;
import com.example.demo.ui.activity.DataBaseActivity;
import com.example.demo.ui.base.BaseFragment;
import com.example.demo.ui.bluetooth.BluetoothActivity;
import com.example.demo.ui.bluetooth.BluetoothLeActivity;
import com.example.demo.ui.camera.AudioActivity;
import com.example.demo.ui.camera.Camera2Activity;
import com.example.demo.ui.camera.CameraActivity;
import com.example.demo.ui.camera.TakePictureActivity;
import com.example.demo.ui.camera.TakeVideoActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DeviceFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityDeviceBinding binding = ActivityDeviceBinding.inflate(inflater, container, false);

        binding.datebase.setOnClickListener(v -> startActivity(new Intent(requireContext(), DataBaseActivity.class)));
        binding.bluetooth.setOnClickListener(v -> startActivity(new Intent(requireContext(), BluetoothActivity.class)));
        binding.bluetoothLe.setOnClickListener(v -> startActivity(new Intent(requireContext(), BluetoothLeActivity.class)));
        binding.audio.setOnClickListener(v -> startActivity(new Intent(requireContext(), AudioActivity.class)));
        binding.camera.setOnClickListener(v -> startActivity(new Intent(requireContext(), CameraActivity.class)));
        binding.camera2.setOnClickListener(v -> startActivity(new Intent(requireContext(), Camera2Activity.class)));
        binding.camerax.setOnClickListener(v -> startActivity(new Intent(requireContext(), TakePictureActivity.class)));
        binding.video.setOnClickListener(v -> startActivity(new Intent(requireContext(), TakeVideoActivity.class)));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
