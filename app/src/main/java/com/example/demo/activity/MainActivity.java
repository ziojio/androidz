package com.example.demo.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.demo.R;
import com.example.demo.activity.bluetooth.BluetoothActivity;
import com.example.demo.activity.bluetooth.BluetoothLeActivity;
import com.example.demo.activity.camera.AudioActivity;
import com.example.demo.activity.camera.Camera2Activity;
import com.example.demo.activity.camera.TakePictureActivity;
import com.example.demo.activity.camera.TakeVideoActivity;
import com.example.demo.activity.databinding.DataBindingActivity;
import com.example.demo.activity.paging.Paging3Activity;
import com.example.demo.activity.web.WebActivity;
import com.example.demo.adapter.ArrayRecyclerViewAdapter;
import com.example.demo.compose.ComposeActivity;
import com.example.demo.databinding.ActivityMainBinding;
import com.example.demo.util.AsyncTask;
import com.example.demo.util.KeyboardWatcher;
import com.example.demo.util.LogUtil;
import com.example.demo.util.Timber;
import com.google.android.material.snackbar.Snackbar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.hjq.permissions.permission.PermissionLists;
import com.hjq.permissions.permission.base.IPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import androidz.util.FileUtil;
import androidz.util.LoadingDialog;
import androidz.util.UtilApp;
import okio.ByteString;


public class MainActivity extends BaseActivity {
    private static final String[] strings = new String[]{
            "execute", "compose", "dataBinding", "snackbar", "popup", "database",
            "dialog", "webview", "http", "rxjava", "paging", "animation",
            "bluetooth", "bluetoothLe", "camera2", "takePicture", "takeVideo", "recordAudio"
    };
    private ActivityMainBinding binding;
    private PopupWindow popupWindow;
    private int count = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        splashScreen.setKeepOnScreenCondition(() -> count > 0);
        new CountDownTimer(count * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                count = 0;
            }
        }.start();
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

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
        XXPermissions.with(this)
                .permission(PermissionLists.getWriteExternalStoragePermission())
                .permission(PermissionLists.getReadMediaImagesPermission())
                .permission(PermissionLists.getReadMediaVisualUserSelectedPermission())
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<IPermission> permissions, boolean allGranted) {
                        Timber.d("onGranted: " + permissions);
                    }

                    @Override
                    public void onDenied(@NonNull List<IPermission> permissions, boolean doNotAskAgain) {
                        Timber.d("onDenied: " + permissions);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            if (popupWindow == null) {
                popupWindow = new PopupWindow(this);
            }
            popupWindow.showPopupWindow(binding.toolbar);
            // popupWindow.showPopupWindow(100, 100);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("ConstantConditions")
    private void execFunc(View view, String cmd) {
        switch (cmd) {
            case "execute" -> {
                Timber.d("execute");
                if (true) {
                    AsyncTask.doAction(new Runnable() {
                        @Override
                        public void run() {
                            Timber.d("getPackageInfo " + UtilApp.getPackageInfo());
                        }
                    });
                }
                if (false) {
                    AsyncTask.doAction(() -> {
                        Timber.d("getExternalStorageState: " + Environment.getExternalStorageState());
                        File downloadDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                        Timber.d("downloadDir: " + downloadDir + ", exists=  " + downloadDir.exists());
                        File documentsDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                        Timber.d("documentsDir: " + documentsDir + ", exists=  " + documentsDir.exists());
                        File path = new File(documentsDir, "hello.txt");
                        Timber.d("getPath " + path.getPath());
                        try (FileOutputStream out = new FileOutputStream(path)) {
                            out.write("hello".getBytes());
                        } catch (IOException e) {
                            Timber.d(e);
                        }
                    });
                }
                if (false) {
                    AsyncTask.doAction(() -> {
                        Calendar date = Calendar.getInstance();
                        File file = new File(LogUtil.getLogDir(this), LogUtil.getLogFileName(date.getTime()));
                        try {
                            Timber.d("MD5: " + ByteString.of(FileUtil.digest(file, "MD5")).hex());
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                if (false) {
                    AsyncTask.doAction(() -> {
                        try {
                            File path = new File(getCacheDir(), "component-library.zip");
                            // File path = new File(getCacheDir(), "HarmonyOS_Icons.zip");
                            FileInputStream fin = new FileInputStream(path);
                            Timber.d("available: " + fin.available());
                            fin.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
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
                // new Handler(Looper.getMainLooper()).postDelayed(() -> {
                //     LoadingDialog.hide();
                //     LoadingDialog.Options options = new LoadingDialog.Options();
                //     options.cancelable = true;
                //     LoadingDialog.showLoading(this, options);
                // }, 3000);
            }
            case "compose" -> startActivity(new Intent(this, ComposeActivity.class));
            case "dataBinding" -> startActivity(new Intent(this, DataBindingActivity.class));
            case "webview" -> startActivity(new Intent(this, WebActivity.class));
            case "snackbar" -> showSnackbar(view);
            case "http" -> startActivity(new Intent(this, HttpActivity.class));
            case "rxjava" -> startActivity(new Intent(this, RxJavaActivity.class));
            case "animation" -> startActivity(new Intent(this, AnimationActivity.class));
            case "paging" -> startActivity(new Intent(this, Paging3Activity.class));
            case "database" -> startActivity(new Intent(this, DatabaseActivity.class));
            case "bluetooth" -> startActivity(new Intent(this, BluetoothActivity.class));
            case "bluetoothLe" -> startActivity(new Intent(this, BluetoothLeActivity.class));
            case "camera2" -> startActivity(new Intent(this, Camera2Activity.class));
            case "takePicture" -> startActivity(new Intent(this, TakePictureActivity.class));
            case "takeVideo" -> startActivity(new Intent(this, TakeVideoActivity.class));
            case "recordAudio" -> startActivity(new Intent(this, AudioActivity.class));
        }
    }

    private void showSnackbar(View v) {
        Timber.d("showSnackbar ");
        Snackbar.make(v, "Snackbar", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(this, R.color.white))
                .setTextColor(ContextCompat.getColor(this, R.color.deep_purple_100))
                .setAction("Ok", v1 -> showToast("Snackbar Ok"))
                .setAnchorView(v)
                .show();
    }

    private void buildInfo() {
        String ver = System.getProperty("java.vm.version");
        Timber.d("java.vm.version: " + ver);
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                Object value = field.get(null);
                if (value != null && value.getClass().isArray()) {
                    Timber.d("Build." + field.getName() + ": " + Arrays.toString((Object[]) value));
                } else {
                    Timber.d("Build." + field.getName() + ": " + value);
                }
            }
        } catch (IllegalAccessException e) {
            Timber.e(e);
        }
        Timber.d("Build.VERSION.RELEASE: " + Build.VERSION.RELEASE);
        Timber.d("Build.VERSION.SDK_INT: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Timber.d("Build.VERSION.SECURITY_PATCH: " + Build.VERSION.SECURITY_PATCH);
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
