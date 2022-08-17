package com.example.demo.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ExifInterface;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;

import com.example.demo.databinding.ActivityCamera2Binding;
import com.example.demo.ui.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import timber.log.Timber;

public class Camera2Activity extends BaseActivity implements SurfaceHolder.Callback, ImageReader.OnImageAvailableListener, TextureView.SurfaceTextureListener {
    private ActivityCamera2Binding binding;
    private CameraDevice mCamera;
    private CameraManager mCameraManager;
    private ImageReader mImageReader;
    private CameraCharacteristics mCharacteristics;
    private CameraCaptureSession mCameraCaptureSession;
    private HandlerThread mHandlerThread;
    private String FACING_BACK = "0";
    private String FACING_FRONT = "1";
    private String mFacing = FACING_FRONT;
    private Handler mHandler;
    private OrientationLiveData relativeOrientation;
    private Size mPreviewSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        binding = ActivityCamera2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.title.setText("相机");
        mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mHandlerThread = new HandlerThread("Camera");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        binding.takePicture.setOnClickListener(v -> {
            if (mCameraCaptureSession != null) {
                takePicture();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            DisplayMetrics metrics = new DisplayMetrics();
            getDisplay().getRealMetrics(metrics);
            Timber.d(" " + metrics);
        }

        // mHandler.post(this::showCameraInfo);
        // binding.surfaceView.getHolder().addCallback(this);
        binding.textureView.setSurfaceTextureListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeCamera();
        mHandlerThread.quit();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Timber.i("surfaceCreated");
        openCamera();
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

    /**
     * 打开相机
     */
    @SuppressLint("MissingPermission")
    private void openCamera() {
        Timber.i("openCamera");
        try {
            mCameraManager.openCamera(mFacing, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    Timber.i("onOpened: " + camera);
                    initCamera2(camera);
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    Timber.e("onDisconnected: " + camera);
                    closeCamera();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    closeCamera();
                    String s;
                    switch (error) {
                        case ERROR_CAMERA_DEVICE:
                            s = "Fatal (device)";
                            break;
                        case ERROR_CAMERA_DISABLED:
                            s = "Device policy";
                            break;
                        case ERROR_CAMERA_IN_USE:
                            s = "Camera in use";
                            break;
                        case ERROR_CAMERA_SERVICE:
                            s = "Fatal (service)";
                            break;
                        case ERROR_MAX_CAMERAS_IN_USE:
                            s = "Maximum cameras in use";
                            break;
                        default:
                            s = "Unknown";
                            break;
                    }
                    Timber.e("onError: " + camera + " error=" + s);
                }
            }, mHandler);
        } catch (CameraAccessException e) {
            Timber.e(e);
        }
    }

    private void initCamera2(CameraDevice camera) {
        Timber.i("initCamera2");
        mCamera = camera;
        try {
            mCharacteristics = mCameraManager.getCameraCharacteristics(camera.getId());
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        // Used to rotate the output media to match device orientation
        runOnUiThread(() -> {
            if (relativeOrientation != null) {
                relativeOrientation.removeObservers(this);
            }
            relativeOrientation = new OrientationLiveData(this, mCharacteristics);
            relativeOrientation.observe(this, new Observer<>() {
                @Override
                public void onChanged(Integer integer) {
                    Timber.i("relativeOrientation onChanged: " + integer);
                }
            });
        });
        StreamConfigurationMap map = mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Timber.i("OutputFormats: " + Arrays.toString(map.getOutputFormats()));
        Size[] sizes = map.getOutputSizes(ImageFormat.JPEG);
        Timber.i("OutputSizes: " + Arrays.toString(sizes));
        Size mPreviewSize = CameraUtil.getPictureSize(sizes, 1280 * 960);
        Timber.i("PictureSize: " + mPreviewSize);
        mImageReader = ImageReader.newInstance(mPreviewSize.getWidth(), mPreviewSize.getHeight(), ImageFormat.JPEG, 2);

        try {
            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //     Surface surface = binding.surfaceView.getHolder().getSurface();
            //     OutputConfiguration outputConfiguration = new OutputConfiguration(surface);
            //      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            //           outputConfiguration.setMirrorMode(OutputConfiguration.MIRROR_MODE_NONE);
            //      }
            //       OutputConfiguration captureOutputConfiguration = new OutputConfiguration(mImageReader.getSurface());
            //
            //     SessionConfiguration sessionConfiguration = new SessionConfiguration(
            //             SessionConfiguration.SESSION_REGULAR,
            //             List.of(outputConfiguration, captureOutputConfiguration),
            //             Executors.newSingleThreadExecutor(),
            //             new CameraCaptureSession.StateCallback() {
            //
            //                 @Override
            //                 public void onConfigured(@NonNull CameraCaptureSession session) {
            //                     Timber.i("onConfigured: session=" + session);
            //                     // This will keep sending the capture request as frequently as possible until the
            //                     // session is torn down or session.stopRepeating() is called
            //                     mCameraCaptureSession = session;
            //                     try {
            //                         CaptureRequest.Builder captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            //                         captureRequest.addTarget(surface);
            //                         mCameraCaptureSession.setRepeatingRequest(captureRequest.build(), null, mHandler);
            //                     } catch (CameraAccessException e) {
            //                         e.printStackTrace();
            //                     }
            //                 }
            //
            //                 @Override
            //                 public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            //                     Timber.e("onConfigureFailed " + session);
            //                 }
            //             });
            //      camera.createCaptureSession(sessionConfiguration);
            // } else {
            Matrix matrix = new Matrix();
            matrix.setScale(-1, 1);
            matrix.postTranslate(binding.textureView.getWidth(), 0);
            binding.textureView.setTransform(matrix);

            SurfaceTexture surfaceTexture = binding.textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface surface = new Surface(surfaceTexture);

            List<Surface> targets = List.of(surface, mImageReader.getSurface());
            camera.createCaptureSession(targets, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    Timber.i("onConfigured: session=" + session);
                    // This will keep sending the capture request as frequently as possible until the
                    // session is torn down or session.stopRepeating() is called
                    mCameraCaptureSession = session;
                    try {
                        CaptureRequest.Builder captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        captureRequest.addTarget(surface);
                        mCameraCaptureSession.setRepeatingRequest(captureRequest.build(), null, mHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Timber.e("onConfigureFailed " + session);
                }
            }, mHandler);
            // }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        Timber.i("closeCamera " + mCamera);
        if (mCamera != null) {
            mCamera.close();
            mCamera = null;
        }
    }

    /**
     * Takes a picture.
     */
    private void takePicture() {
        try {
            CaptureRequest.Builder captureBuilder = mCamera.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
            captureBuilder.addTarget(mImageReader.getSurface());

            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, relativeOrientation.getValue());

            int sensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            // Timber.i("SensorOrientation " + sensorOrientation);
            // Timber.i("DisplayRotation " + getRotationDegree(getDisplay().getRotation()));

            mImageReader.setOnImageAvailableListener(this, mHandler);
            mCameraCaptureSession.capture(captureBuilder.build(), new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                    Timber.i("onCaptureStarted: timestamp=" + timestamp + " frameNumber=" + frameNumber);

                }

                @Override
                public void onCaptureFailed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureFailure failure) {
                    super.onCaptureFailed(session, request, failure);
                    Timber.e("onCaptureFailed: failure=" + failure + " Reason=" + failure.getReason());
                }

                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    Timber.i("onCaptureCompleted: result=" + result);
                    // mImageReader.setOnImageAvailableListener(null, null);
                }
            }, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onImageAvailable(ImageReader reader) {
        Timber.i("onImageAvailable: " + reader);
        try {
            Image image = reader.acquireLatestImage();
            Timber.i("Width: " + image.getWidth());
            Timber.i("Height: " + image.getHeight());
            Timber.i("Format: " + image.getFormat());
            File file = CameraUtil.createPictureFile(getCacheDir(), "IMG", "jpg");
            OutputStream output = new FileOutputStream(file);
            Timber.i("file: " + file.getAbsolutePath());
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            Timber.i("capacity: " + buffer.capacity());
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            image.close();

            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
            // Matrix matrix = new Matrix();
            // matrix.postRotate(relativeOrientation.getValue());
            // bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            boolean result = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
            Timber.i("result: " + result);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                ExifInterface exif = new ExifInterface(file);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, "270");
                exif.saveAttributes();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCameraInfo() {
        try {
            String[] ids = mCameraManager.getCameraIdList();
            for (String id : ids) {
                Timber.d("------------------ Camera  ------------------");
                Timber.d("Camera.Id " + id);
                CameraCharacteristics characteristics = mCameraManager.getCameraCharacteristics(id);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    Timber.d("CameraCharacteristics.INFO_VERSION " + characteristics.get(CameraCharacteristics.INFO_VERSION));
                }
                Timber.d("CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL " + characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));
                Timber.d("CameraCharacteristics.LENS_FACING " + characteristics.get(CameraCharacteristics.LENS_FACING));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Timber.d("CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE " + characteristics.get(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE).toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
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

    @Override
    public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
        openCamera();
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
        closeCamera();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

    }
}