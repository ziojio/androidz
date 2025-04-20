package com.example.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.activity.bluetooth.BluetoothActivity;
import com.example.demo.activity.bluetooth.BluetoothLeActivity;
import com.example.demo.activity.camera.AudioActivity;
import com.example.demo.activity.camera.Camera2Activity;
import com.example.demo.activity.camera.CameraActivity;
import com.example.demo.activity.camera.TakePictureActivity;
import com.example.demo.activity.camera.TakeVideoActivity;
import com.example.demo.activity.databinding.DataBindingActivity;
import com.example.demo.activity.paging.Paging3Activity;
import com.example.demo.activity.web.WebActivity;
import com.example.demo.adapter.ArrayRecyclerViewAdapter;
import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.util.AsyncTask;
import com.example.demo.util.KeyboardWatcher;
import com.example.demo.util.LogUtil;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidz.util.FileUtil;
import androidz.util.LoadingDialog;
import composex.ui.ComposeActivity;
import okio.ByteString;
import timber.log.Timber;


public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String[] strings = new String[]{
            "execute", "snackbar", "popup", "database", "dialog",
            "compose", "animation", "dataBinding",
            "http", "rxjava", "webview", "paging",
            "bluetooth", "bluetoothLe", "audio", "camera", "camera2", "camerax", "video"
    };
    private PopupWindow popupWindow;
    private int count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        new CountDownTimer(count * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                count = 0;
            }
        }.start();
        splashScreen.setKeepOnScreenCondition(() -> count > 0);
        XXPermissions.with(this)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        Timber.d("WRITE_EXTERNAL_STORAGE Granted: " + allGranted);
                    }
                });
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.recyclerview.setAdapter(new ArrayRecyclerViewAdapter<>(strings, R.layout.item_home_fun) {
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(item);
                textView.setOnClickListener(v -> execFunc(v, item));
            }
        });

        KeyboardWatcher.with(this).setListener(new KeyboardWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {
                Timber.d("onSoftKeyboardOpened: keyboardHeight=" + keyboardHeight);
            }

            @Override
            public void onSoftKeyboardClosed() {
                Timber.d("onSoftKeyboardClosed");
            }
        });
        buildInfo();
        showDisplay();
    }

    private void execFunc(View view, String cmd) {
        switch (cmd) {
            case "execute" -> {
                Log.d(TAG, "execute");
                if (true) {
                    AsyncTask.doAction(() -> {
                        Timber.d("getExternalStorageState " + Environment.getExternalStorageState());
                        File parent = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        Timber.d("getExternalFilesDir " + parent);
                        if (!parent.exists()) {
                            parent.mkdirs();
                        }
                        File path = new File(parent, "hello.txt");
                        Timber.d("getPath " + path.getPath());
                        try (FileOutputStream out = new FileOutputStream(path)) {
                            out.write("hello".getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                if (false) {
                    try {
                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (false) {
                    Calendar date = Calendar.getInstance();
                    File file = new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(date.getTime()));
                    try {
                        Timber.d("MD5: " + ByteString.of(FileUtil.digest(file, "MD5")).hex());
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (false) {
                    try {
                        File path = new File(getCacheDir(), "component-library.zip");
                        File path2 = new File(getCacheDir(), "HarmonyOS_Icons.zip");
                        File path3 = new File(getCacheDir(), "CyberGuard.yaml");
                        FileInputStream fin = new FileInputStream(path3);
                        Timber.d("available: " + fin.available());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "popup" -> {
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(this);
                }
                popupWindow.showPopupWindow(view);
                // popupWindow.showPopupWindow(100, 100);
            }
            case "dialog" -> {
                LoadingDialog.showLoading(this);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    LoadingDialog.hide();

                    LoadingDialog.Options options = new LoadingDialog.Options();
                    options.cancelable = true;
                    LoadingDialog.showLoading(this, options);
                }, 3000);
            }
            case "snackbar" -> showSnackbar(view);
            case "webview" -> startActivity(new Intent(this, WebActivity.class));
            case "database" -> startActivity(new Intent(this, DatabaseActivity.class));
            case "compose" -> startActivity(new Intent(this, ComposeActivity.class));
            case "http" -> startActivity(new Intent(this, HttpActivity.class));
            case "animation" -> startActivity(new Intent(this, AnimationActivity.class));
            case "dataBinding" -> startActivity(new Intent(this, DataBindingActivity.class));
            case "rxjava" -> startActivity(new Intent(this, RxJavaActivity.class));
            case "paging" -> startActivity(new Intent(this, Paging3Activity.class));
            case "bluetooth" -> startActivity(new Intent(this, BluetoothActivity.class));
            case "bluetoothLe" -> startActivity(new Intent(this, BluetoothLeActivity.class));
            case "audio" -> startActivity(new Intent(this, AudioActivity.class));
            case "camera" -> startActivity(new Intent(this, CameraActivity.class));
            case "camera2" -> startActivity(new Intent(this, Camera2Activity.class));
            case "camerax" -> startActivity(new Intent(this, TakePictureActivity.class));
            case "video" -> startActivity(new Intent(this, TakeVideoActivity.class));
        }
    }

    private void showSnackbar(View v) {
        Timber.d("showSnackbar ");
        Snackbar.make(v, "Snackbar", Snackbar.LENGTH_SHORT)
                // .setBackgroundTint(this.getColor(R.color.white))
                // .setTextColor(this.getColor(R.color.deep_purple_100))
                .setAction("Ok", v1 -> showToast("Snackbar Ok"))
                .setAnchorView(v)
                .show();
    }

    private void buildInfo() {
        Timber.d("Build: ");
        String ver = System.getProperty("java.vm.version");
        Timber.d("java.vm.version: " + ver);
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                Timber.d(field.getName() + ": " + field.get(null));
            }
            Timber.d("Build.VERSION: ");
            fields = Build.VERSION.class.getDeclaredFields();
            for (Field field : fields) {
                Timber.d(field.getName() + ": " + field.get(null));
            }
        } catch (IllegalAccessException e) {
            Timber.e(e);
        }
    }

    void showDialog() {
        Timber.d("openDialog");
        new AlertDialog.Builder(this)
                .setTitle("AlertDialog")
                .setMessage("This is AlertDialog")
                .setCancelable(true)
                .setPositiveButton("LoadingDialog", (dialog12, which) -> {

                })
                .setNegativeButton("LoadingDialogFragment", (dialog1, which) -> {

                })
                .show();
    }

    void showInternet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Timber.d("isDefaultNetworkActive: " + manager.isDefaultNetworkActive());
            Network network = manager.getActiveNetwork();
            Timber.d("network: " + network);
            if (network == null) {
                return;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return;
            }
            Timber.d("NetworkCapabilities: " + capabilities);
        } else {
            Timber.w("showInternet: SDK_INT < Build.VERSION_CODES.M");
        }
    }

    void showDisplay() {
        WindowManager wm = ContextCompat.getSystemService(this, WindowManager.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        Timber.d("getMetrics: " + displayMetrics);
        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        Timber.d("getRealMetrics: " + displayMetrics);
    }
}
