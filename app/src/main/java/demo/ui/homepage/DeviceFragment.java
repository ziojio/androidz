package demo.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import demo.databinding.ActivityDeviceBinding;
import demo.ui.activity.DataBaseActivity;
import demo.ui.base.BaseFragment;
import demo.ui.bluetooth.BluetoothActivity;
import demo.ui.bluetooth.BluetoothLeActivity;
import demo.ui.camera.AudioActivity;
import demo.ui.camera.Camera2Activity;
import demo.ui.camera.CameraActivity;
import demo.ui.camera.TakePictureActivity;
import demo.ui.camera.TakeVideoActivity;

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
