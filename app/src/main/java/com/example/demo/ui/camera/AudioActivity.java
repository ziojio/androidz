package com.example.demo.ui.camera;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioRecordingConfiguration;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.View;

import com.example.demo.databinding.ActivityAudioBinding;
import com.example.demo.ui.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import timber.log.Timber;


public class AudioActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAudioBinding binding;
    private AudioRecord audioRecord;
    private int sampleRateHz = 48000;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
    private int bufferSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        binding = ActivityAudioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.titlebar.title.setText("录音");

        AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        bufferSize = AudioRecord.getMinBufferSize(sampleRateHz, channelConfig, audioFormat);

        binding.play.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            audioRecord.release();
        }
    }

    @Override
    public void onClick(View v) {
        if (audioRecord == null || audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
            record();
            binding.time.setBase(SystemClock.elapsedRealtime());
            binding.time.start();
        } else {
            stop();
            binding.time.stop();
        }
    }

    @SuppressLint("MissingPermission")
    private void record() {
        Timber.i("record");
        if (audioRecord == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioRecord = new AudioRecord.Builder()
                        .setAudioSource(MediaRecorder.AudioSource.MIC)
                        .setAudioFormat(new AudioFormat.Builder()
                                .setEncoding(audioFormat)
                                .setSampleRate(sampleRateHz)
                                .setChannelMask(channelConfig)
                                .build())
                        .setBufferSizeInBytes(bufferSize * 2)
                        .build();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                audioRecord.registerAudioRecordingCallback(Executors.newSingleThreadExecutor(), new AudioManager.AudioRecordingCallback() {
                    @Override
                    public void onRecordingConfigChanged(List<AudioRecordingConfiguration> configs) {
                        super.onRecordingConfigChanged(configs);
                        Timber.i("AudioRecord onRecordingConfigChanged ");
                    }
                });
            }
        }
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED) {
            audioRecord.startRecording();
            readAudio();
        }
    }

    private void stop() {
        Timber.i("stop ");
        if (audioRecord != null && audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
            audioRecord.stop();
        }
    }

    private void readAudio() {
        File pcmFile = createPcmFile();
        if (pcmFile.exists()) {
            pcmFile.delete();
        }
        final byte[] buffer = new byte[bufferSize];
        audioRecord.startRecording();

        new Thread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream stream = null;
                try {
                    stream = new FileOutputStream(pcmFile);
                    while (audioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                        int readStatus = audioRecord.read(buffer, 0, bufferSize);
                        Timber.d("readStatus=" + readStatus);
                        if (readStatus > 0) {
                            stream.write(buffer, 0, readStatus);
                        }
                    }
                } catch (IOException e) {
                    Timber.e(e);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }

    private File createPcmFile() {
        File pcmFile = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "raw.pcm");
        Timber.d("pcm file=" + pcmFile);
        return pcmFile;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];
                if (perm.equals(Manifest.permission.RECORD_AUDIO) && grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    showToast("录音未授权");
                    finish();
                }
                Timber.d(perm + " " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "GRANTED" : "DENIED"));
            }
        }
    }

}