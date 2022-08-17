package com.example.demo.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.demo.R;
import com.example.demo.databinding.ActivityTakePictureBinding;
import com.example.demo.ui.base.BaseActivity;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.LifecycleCameraController;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import timber.log.Timber;


public class TakePictureActivity extends BaseActivity {
    private ActivityTakePictureBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageCapture imageCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }
        binding = ActivityTakePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.title.setText("拍照");

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
        binding.takePicture.setImageResource(R.drawable.ic_camera);
        binding.takePicture.setOnClickListener(v -> {
            if (imageCapture != null) {
                File imgFile = new File(getExternalCacheDir(), "takePicture_" + format.format(new Date()) + ".jpg");
                ImageCapture.OutputFileOptions.Builder outputFileOptions =
                        new ImageCapture.OutputFileOptions.Builder(imgFile);

                imageCapture.takePicture(outputFileOptions.build(),
                        Executors.newSingleThreadExecutor(), new ImageCapture.OnImageSavedCallback() {
                            @Override
                            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                                Timber.d("onImageSaved: " + outputFileResults.getSavedUri());
                                showToast("onImageSaved: " + outputFileResults.getSavedUri());
                            }

                            @Override
                            public void onError(@NonNull ImageCaptureException exception) {
                                Timber.d("onError: " + exception.getMessage());
                            }
                        });
            }
        });

        bindCamera();
    }

    private void bindCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                bindPreview(cameraProviderFuture.get());
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        imageCapture = new ImageCapture.Builder().build();

        cameraProvider.bindToLifecycle(this, CameraSelector.DEFAULT_BACK_CAMERA,
                preview, imageCapture);
    }

    private void useCameraController() {
        LifecycleCameraController cameraController = new LifecycleCameraController(this);
        cameraController.setCameraSelector(CameraSelector.DEFAULT_FRONT_CAMERA);
        cameraController.bindToLifecycle(this);
        binding.previewView.setController(cameraController);
    }

    /**
     * @param width  - preview width
     * @param height - preview height
     * @return suitable aspect ratio
     */
    private int aspectRatio(int width, int height) {
        float previewRatio = Math.max(width, height) * 1f / Math.min(width, height);
        if (Math.abs(previewRatio - AspectRatio.RATIO_4_3)
                <= Math.abs(previewRatio - AspectRatio.RATIO_16_9)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (permission.equals(Manifest.permission.CAMERA) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("摄像头未授权");
                    finish();
                }
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Timber.i(permission + " GRANTED");
                } else {
                    Timber.w(permission + " DENIED");
                }
            }
        }
    }

}