package com.example.demo.ui.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.demo.R;
import com.example.demo.databinding.ActivityTakePictureBinding;
import com.example.demo.ui.base.BaseActivity;
import com.example.demo.util.DebugLifecycleObserver;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.FileOutputOptions;
import androidx.camera.video.PendingRecording;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.lifecycle.LifecycleOwner;
import timber.log.Timber;

public class TakeVideoActivity extends BaseActivity {
    private ActivityTakePictureBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private VideoCapture<Recorder> videoCapture;
    private Recording recording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 0);
        }
        binding = ActivityTakePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.title.setText("视频");

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));

        binding.takePicture.setOnClickListener(v -> {
            if (videoCapture == null) {
                return;
            }
            if (recording == null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss", Locale.CHINA);
                File videoFile = new File(getExternalCacheDir(),
                        "takeVideo_" + format.format(new Date()) + ".mp4");
                PendingRecording pendingRecording = videoCapture.getOutput().prepareRecording(this,
                        new FileOutputOptions.Builder(videoFile).setFileSizeLimit(1024 * 1024 * 100).build());
                pendingRecording.withAudioEnabled();

                recording = pendingRecording.start(ContextCompat.getMainExecutor(this), new Consumer<VideoRecordEvent>() {
                    @Override
                    public void accept(VideoRecordEvent videoRecordEvent) {
                        Timber.d("----------------------------------------------------");
                        Timber.d("videoRecordEvent=" + videoRecordEvent);
                        Timber.d("getFileSizeLimit=" + videoRecordEvent.getOutputOptions().getFileSizeLimit());
                        Timber.d("getRecordedDurations=" + TimeUnit.NANOSECONDS.toSeconds(
                                videoRecordEvent.getRecordingStats().getRecordedDurationNanos()));
                        Timber.d("getNumBytesRecorded=" + videoRecordEvent.getRecordingStats().getNumBytesRecorded());
                        Timber.d("getAudioStats=" + videoRecordEvent.getRecordingStats().getAudioStats());
                    }
                });
                binding.takePicture.setImageResource(R.drawable.ic_stop_circle);
            } else {
                recording.stop();
                recording = null;
                binding.takePicture.setImageResource(R.drawable.ic_play_circle);
            }
        });

        getLifecycle().addObserver(new DebugLifecycleObserver());
        // getLifecycle().addObserver(new DefaultLifecycleObserver() {
        //     @Override
        //     public void onResume(@NonNull LifecycleOwner owner) {
        //         DefaultLifecycleObserver.super.onResume(owner);
        //         if (recording != null) {
        //             recording.resume();
        //         }
        //     }
        //
        //     @Override
        //     public void onPause(@NonNull LifecycleOwner owner) {
        //         DefaultLifecycleObserver.super.onPause(owner);
        //         if (recording != null) {
        //             recording.pause();
        //         }
        //     }
        //
        //     @Override
        //     public void onDestroy(@NonNull LifecycleOwner owner) {
        //         DefaultLifecycleObserver.super.onDestroy(owner);
        //         if (recording != null) {
        //             recording.stop();
        //         }
        //     }
        // });
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        Recorder recorder = new Recorder.Builder().build();
        videoCapture = VideoCapture.withOutput(recorder);

        cameraProvider.bindToLifecycle((LifecycleOwner) this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                videoCapture);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (perm.equals(Manifest.permission.CAMERA) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("摄像头未授权");
                    finish();
                }
                Timber.d(perm + " " +
                        (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
        }
    }
}