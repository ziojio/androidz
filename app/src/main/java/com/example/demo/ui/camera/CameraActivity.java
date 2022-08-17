package com.example.demo.ui.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.demo.databinding.ActivityCameraBinding;
import com.example.demo.ui.base.BaseActivity;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import timber.log.Timber;

public class CameraActivity extends BaseActivity implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        ActivityCameraBinding binding = ActivityCameraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.title.setText("相机");

        SurfaceView surfaceView = binding.surfaceView;
        surfaceView.getHolder().addCallback(this);

        binding.takePicture.setOnClickListener(v -> {
            setCameraDisplayOrientation(this, 1, mCamera);
        });
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.i("surfaceCreated");
        openCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Timber.i("surfaceChanged format=%d, width=%d, height=%d", format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Timber.i("surfaceDestroyed");
        closeCamera();
    }

    byte[] buffer = new byte[ImageFormat.getBitsPerPixel(ImageFormat.NV21) * 800 * 600 / 8];

    /**
     * 打开相机
     */
    private void openCamera(SurfaceHolder holder) {
        Timber.i("openCamera");
        try {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Timber.e("相机开始失败");
            closeCamera();
            return;
        }
        Camera.Parameters params = mCamera.getParameters();
        // setFocusMode(params);
        // setCameraRotation(mCamera, Camera.CameraInfo.CAMERA_FACING_FRONT);

        // 获取支持的最大的 4:3 比例尺寸图片尺寸
        Camera.Size maxSize = getMaxPictureSize(params);
        // 设置预览尺寸为图像尺寸
        Timber.i("openCamera: w = " + maxSize.width + " h =  " + maxSize.height);
        params.setPreviewSize(maxSize.width, maxSize.height);
        params.setPictureSize(maxSize.width, maxSize.height);
        params.set("preview-flip", "flip-v");
        // 设置预览编码图像编码格式为 NV21
        params.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(params);
        mCamera.setPreviewCallbackWithBuffer(this);
        mCamera.addCallbackBuffer(buffer);
        setCameraDisplayOrientation(this, 1, mCamera);
        mCamera.startPreview();
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        Timber.i("closeCamera: " + mCamera);
        synchronized (this) {
            if (mCamera == null) {
                return;
            }
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (perm.equals(Manifest.permission.CAMERA) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("摄像头未授权");
                    finish();
                }
                Timber.d(perm + " " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
        }
    }

    /**
     * 获取最大支持 4:3 的图像尺寸
     */
    private Camera.Size getMaxPictureSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPictureSizes();
        int maxArea = -1;
        Camera.Size maxSize = null;

        for (Camera.Size size : previewSizes) {
            Timber.i("Support size -> " + size.width + " x " + size.height);

            int gcd = gcd(size.width, size.height);
            int w = size.width / gcd;
            int h = size.height / gcd;
            if (size.width > 1000) {
                continue;
            }
            if (w == 4 && h == 3 && (size.width * size.height) > maxArea) {
                maxArea = size.width * size.height;
                maxSize = size;
            }
        }
        if (maxSize != null) {
            Timber.i("Max 4:3 -> " + maxSize.width + " x " + maxSize.height);
        }
        return maxSize;
    }

    /**
     * 计算最大公约数
     */
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}