package com.example.demo.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import timber.log.Timber;


public class FileLogTree extends Timber.DebugTree implements Handler.Callback {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);
    private HandlerThread handlerThread;
    private Handler handler;
    private FileWriter fileWriter;

    public FileLogTree(File file) {
        try {
            Objects.requireNonNull(file);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
            handlerThread = new HandlerThread("FileLog");
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper(), this);
            fileWriter = new FileWriter(file, true);
        } catch (Exception e) {
            Log.e("FileLogTree", "file[" + file + "]", e);
        }
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (handler != null) {
            // if (t != null) {
            //     message += " " + t.getMessage();
            // }
            String log = dateFormat.format(new Date()) + " " + typeInfo(priority) + " " + tag + ": " + message + "\n";
            handler.sendMessage(Message.obtain(handler, 0, log));
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        if (fileWriter != null) {
            try {
                fileWriter.write((String) msg.obj);
                fileWriter.flush();
            } catch (IOException ignored) {
            }
        }
        return true;
    }

    public void release() {
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
            handler = null;
        }
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException ignored) {
            }
            fileWriter = null;
        }
    }

    private String typeInfo(int priority) {
        return switch (priority) {
            case Log.VERBOSE -> "V";
            case Log.INFO -> "I";
            case Log.WARN -> "W";
            case Log.ERROR -> "E";
            case Log.ASSERT -> "A";
            default -> "D";
        };
    }
}