package com.example.demo.ui.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "CameraSurfaceView";
    private static final int MAX_FACES = 2;
    private final ByteArrayOutputStream mBitmapOutput = new ByteArrayOutputStream();
    private Camera mCamera;
    private Camera.PreviewCallback previewCallback;
    private Camera.PictureCallback pictureCallback;
    private boolean isPreviewing;
    private boolean isBack;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private float aspectRatio = 4 / 3f;
    private FaceDetector faceDetector;

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void draw(Canvas canvas) {
        // if (Build.VERSION.SDK_INT >= 26) {
        //     canvas.clipPath(clipPath);
        // } else {
        //     canvas.clipPath(clipPath, Region.Op.REPLACE);
        // }
        super.draw(canvas);
    }

    private void init() {
        setFocusable(true);
        setFocusableInTouchMode(true);
        getHolder().addCallback(this);
    }

    public void start() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    public void stop() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    public void setCameraId(int cameraId) {
        this.cameraId = cameraId;
    }

    public void setAspectRatio(int aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    private void openCamera() {
        // 打开相机
        try {
            mCamera = Camera.open(cameraId);
            mCamera.setPreviewDisplay(getHolder());
        } catch (IOException e) {
            closeCamera();
            return;
        }
        Camera.Parameters params = mCamera.getParameters();
        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.size() > 0) {
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }

        // 获取支持的最大的 4:3 比例尺寸图片尺寸
        Camera.Size size = getMaxPictureSize(params);
        // 设置预览尺寸为图像尺寸
        Log.i(TAG, "setPreviewSize: w=" + size.width + " h=" + size.height);
        params.setPreviewSize(size.width, size.height);
        params.setPictureSize(size.width, size.height);
        // params.setPreviewFormat(ImageFormat.NV21);
        mCamera.setParameters(params);
        // 获取相机应旋转角度
        mCamera.setDisplayOrientation(getRotateAngle(cameraId));
        mCamera.setPreviewCallbackWithBuffer(this);
        int bufferSize = ImageFormat.getBitsPerPixel(ImageFormat.NV21) * size.width * size.height / 8;
        mCamera.addCallbackBuffer(new byte[bufferSize]);
        mCamera.addCallbackBuffer(new byte[bufferSize]);
        mCamera.startPreview();
        isPreviewing = true;
    }

    private int getRotateAngle(int CAMERA_ID) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(CAMERA_ID, info);
        // 获取当前手机的旋转角度
        int rotation = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
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
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 获取最大支持 4:3 图像尺寸
     */
    private Camera.Size getMaxPictureSize(Camera.Parameters params) {
        List<Camera.Size> previewSizes = params.getSupportedPictureSizes();
        Camera.Size size = null;
        int maxArea = 0;

        for (Camera.Size size1 : previewSizes) {
            if (size == null) size = size1;
            Log.i(TAG, "SupportedPictureSizes -> " + size1.width + " x " + size1.height);
            float w = Math.max(size1.width, size1.height);
            float h = Math.min(size1.width, size1.height);
            if (w / h == aspectRatio && w * h > maxArea) {
                maxArea = (int) (w * h);
                size = size1;
            }
        }
        return size;
    }

    /**
     * 关闭相机
     */
    private void closeCamera() {
        Log.i(TAG, "closeCamera:");
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
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (previewCallback != null) {
            previewCallback.onPreviewFrame(data, camera);
        }
        mCamera.addCallbackBuffer(data);
    }

    /**
     * 计算最大公约数
     */
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public void takePhoto() {
        if (mCamera != null) {
            try {
                mCamera.takePicture(null, null, null, pictureCallback);
            } catch (Exception exception) {
                Log.i(TAG, "takePhoto: " + exception.toString());
            }
        }
    }

    public void setPictureCallBack(Camera.PictureCallback callback) {
        this.pictureCallback = callback;
    }

    public void setPreviewCallback(Camera.PreviewCallback callback) {
        this.previewCallback = callback;
    }

    private void checkFace(byte[] mData, Camera camera) {
        if (camera != null) {
            Camera.Size size = camera.getParameters().getPreviewSize();

            YuvImage yuvImage = new YuvImage(mData, ImageFormat.NV21, size.width, size.height, null);
            yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 100, mBitmapOutput);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;//必须设置为565，否则无法检测
            Matrix matrix = new Matrix();
            matrix.preScale(-1.0f, 1.0f);
            matrix.preRotate(270.0f);

            byte[] bytes = mBitmapOutput.toByteArray();
            mBitmapOutput.reset();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (bitmap != null) {
                // bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                // faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACES);
                // FaceDetector.Face[] face = new FaceDetector.Face[MAX_FACES];
                // int faces = faceDetector.findFaces(bitmap, face);
                // if (faceDetectorListener != null) {
                //     faceDetectorListener.onFindFaces(faces, face);
                // }
            }
        }
    }

    public void turnCamera() {
        closeCamera();
        if (isBack) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
            openCamera();
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
            openCamera();
        }
    }

}

