package com.example.demo.ui.camera;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.text.TextUtils;
import android.util.Size;
import android.view.Surface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CameraUtil {

    public static File createPictureFile(File dir, String filename, String extension) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CHINA);
        String name = TextUtils.isEmpty(filename) ? "" : filename + "_";
        name += format.format(new Date()) + "." + extension;
        return new File(dir, name);
    }

    public static boolean isFront(CameraManager cameraManager, CameraDevice cameraDevice) {
        boolean facingFront = false;
        try {
            CameraCharacteristics c = cameraManager.getCameraCharacteristics(cameraDevice.getId());
            facingFront = CameraCharacteristics.LENS_FACING_FRONT == c.get(CameraCharacteristics.LENS_FACING);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return facingFront;
    }

    public static boolean isFront(CameraCharacteristics c) {
        return CameraCharacteristics.LENS_FACING_FRONT == c.get(CameraCharacteristics.LENS_FACING);
    }

    public static boolean isBack(CameraManager cameraManager, CameraDevice cameraDevice) {
        boolean facingBack = false;
        try {
            CameraCharacteristics c = cameraManager.getCameraCharacteristics(cameraDevice.getId());
            facingBack = CameraCharacteristics.LENS_FACING_BACK == c.get(CameraCharacteristics.LENS_FACING);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return facingBack;
    }

    public static boolean isBack(CameraCharacteristics c) {
        return CameraCharacteristics.LENS_FACING_BACK == c.get(CameraCharacteristics.LENS_FACING);
    }

    public static int getJpegOrientation(CameraCharacteristics c, int deviceOrientation) {
        if (deviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN)
            return 0;
        int sensorOrientation = c.get(CameraCharacteristics.SENSOR_ORIENTATION);

        // Round device orientation to a multiple of 90
        deviceOrientation = (deviceOrientation + 45) / 90 * 90;

        // Reverse device orientation for front-facing cameras
        boolean facingFront = c.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT;
        if (facingFront) deviceOrientation = -deviceOrientation;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        int jpegOrientation = (sensorOrientation + deviceOrientation + 360) % 360;

        return jpegOrientation;
    }

    /**
     * 获取 4:3 图像尺寸
     */
    public static Size getPictureSize(Size[] previewSizes, int maxSize) {
        Size retSize = null;
        int curSize = 0;
        for (Size size : previewSizes) {
            int width = size.getWidth();
            int height = size.getHeight();

            int max = width * height;
            if (max > maxSize) continue;

            int gcd = gcd(width, height);
            int w = width / gcd;
            int h = height / gcd;
            if (w == 4 && h == 3) {
                if (max > curSize) {
                    curSize = max;
                    retSize = size;
                }
            }
        }
        if (retSize == null)
            retSize = previewSizes[0];
        return retSize;
    }

    /**
     * 计算最大公约数
     */
    private static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static int getRotationDegree(int rotation) {
        int degree;
        switch (rotation) {
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
            case Surface.ROTATION_0:
            default:
                degree = 0;
                break;
        }
        return degree;
    }
}
