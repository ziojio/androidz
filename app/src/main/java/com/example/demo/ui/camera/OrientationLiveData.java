package com.example.demo.ui.camera;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.view.OrientationEventListener;
import android.view.Surface;

import androidx.lifecycle.LiveData;

/**
 * Calculates closest 90-degree orientation to compensate for the device
 * rotation relative to sensor orientation, i.e., allows user to see camera
 * frames with the expected orientation.
 */
public class OrientationLiveData extends LiveData<Integer> {
    private final OrientationEventListener listener;

    public OrientationLiveData(Context context, CameraCharacteristics characteristics) {
        listener = new OrientationEventListener(context.getApplicationContext()) {
            @Override
            public void onOrientationChanged(int orientation) {
                int rotation = 0;
                if (orientation <= 45) {
                    rotation = 0;
                } else if (orientation <= 135) {
                    rotation = Surface.ROTATION_90;
                } else if (orientation <= 225) {
                    rotation = Surface.ROTATION_180;
                } else if (orientation <= 315) {
                    rotation = Surface.ROTATION_270;
                }
                int relative = computeRelativeRotation(characteristics, rotation);
                if (getValue() == null || relative != getValue()) {
                    postValue(relative);
                }
            }
        };
    }

    /**
     * Computes rotation required to transform from the camera sensor orientation to the
     * device's current orientation in degrees.
     *
     * @param characteristics the [CameraCharacteristics] to query for the sensor orientation.
     * @param surfaceRotation the current device orientation as a Surface constant
     * @return the relative rotation from the camera sensor to the current device orientation.
     */
    private static int computeRelativeRotation(CameraCharacteristics characteristics, int surfaceRotation) {
        int sensorOrientationDegrees = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

        int deviceOrientationDegrees = 0;
        switch (surfaceRotation) {
            case Surface.ROTATION_90:
                deviceOrientationDegrees = 90;
                break;
            case Surface.ROTATION_180:
                deviceOrientationDegrees = 180;
                break;
            case Surface.ROTATION_270:
                deviceOrientationDegrees = 270;
                break;
        }

        // Reverse device orientation for front-facing cameras
        int sign = characteristics.get(CameraCharacteristics.LENS_FACING)
                == CameraCharacteristics.LENS_FACING_FRONT ? 1 : -1;

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientationDegrees - (deviceOrientationDegrees * sign) + 360) % 360;
    }

    protected void onActive() {
        super.onActive();
        listener.enable();
    }

    protected void onInactive() {
        super.onInactive();
        listener.disable();
    }
}
