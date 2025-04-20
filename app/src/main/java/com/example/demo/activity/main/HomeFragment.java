package com.example.demo.activity.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.example.demo.R;
import com.example.demo.activity.AnimationActivity;
import com.example.demo.activity.BaseFragment;
import com.example.demo.activity.DataBaseActivity;
import com.example.demo.activity.HttpActivity;
import com.example.demo.activity.RxJavaActivity;
import com.example.demo.activity.bluetooth.BluetoothActivity;
import com.example.demo.activity.bluetooth.BluetoothLeActivity;
import com.example.demo.activity.camera.AudioActivity;
import com.example.demo.activity.camera.Camera2Activity;
import com.example.demo.activity.camera.CameraActivity;
import com.example.demo.activity.camera.TakePictureActivity;
import com.example.demo.activity.camera.TakeVideoActivity;
import com.example.demo.activity.databinding.DataBindingActivity;
import com.example.demo.activity.edit.EditActivity;
import com.example.demo.activity.paging.Paging3Activity;
import com.example.demo.adapter.ArrayRecyclerViewAdapter;
import com.example.demo.databinding.ActivityHomeBinding;
import com.example.demo.di.MainHandler;
import com.example.demo.ktx.KotlinActivity;
import com.example.demo.log.LogUtil;
import com.example.demo.util.AsyncTask;
import com.example.demo.util.KeyboardWatcher;
import com.example.demo.web.WebActivity;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import androidz.util.FileUtil;
import androidz.util.LoadingDialog;
import composex.ui.ComposeActivity;
import okio.ByteString;
import timber.log.Timber;


public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";

    private static final String[] strings = new String[]{
            "execute", "snackbar", "popup", "database", "dialog",
            "compose", "animation", "dataBinding", "edit",
            "http", "ktx", "rxJava", "webview", "paging",
            "bluetooth", "bluetoothLe", "audio", "camera", "camera2", "camerax", "video"
    };

    MainHandler mHandler = new MainHandler();
    HomePopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ActivityHomeBinding.inflate(inflater, container, false).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActivityHomeBinding binding = ActivityHomeBinding.bind(view);
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerview.setAdapter(new ArrayRecyclerViewAdapter<>(strings, R.layout.item_home_fun) {
            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, @NonNull String item) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(item);
                textView.setOnClickListener(v -> execFunc(v, item));
            }
        });

        KeyboardWatcher.with(requireActivity()).setListener(new KeyboardWatcher.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeight) {
                Timber.d("onSoftKeyboardOpened: keyboardHeight=" + keyboardHeight);
            }

            @Override
            public void onSoftKeyboardClosed() {
                Timber.d("onSoftKeyboardClosed");
            }
        });
    }

    private void execFunc(View view, String cmd) {
        switch (cmd) {
            case "execute" -> {
                Log.d(TAG, "execute");
                // logBuildInfo();
                if (true) {
                    AsyncTask.doAction(() -> {
                        Timber.d("getExternalStorageState " + Environment.getExternalStorageState());
                        File parent = getContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
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
                    Context context = getContext();
                    try {
                        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    } catch (NameNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (false) {
                    Calendar date = Calendar.getInstance();
                    File file = new File(LogUtil.getLogDir(requireContext()), LogUtil.getLogFileName(date.getTime()));
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
                        File path = new File(getContext().getCacheDir(), "component-library.zip");
                        File path2 = new File(getContext().getCacheDir(), "HarmonyOS_Icons.zip");
                        File path3 = new File(getContext().getCacheDir(), "CyberGuard.yaml");
                        FileInputStream fin = new FileInputStream(path3);
                        Timber.d("available: " + fin.available());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            case "popup" -> {
                if (popupWindow == null) {
                    popupWindow = new HomePopupWindow(requireActivity());
                }
                popupWindow.showPopupWindow(view);
                // popupWindow.showPopupWindow(100, 100);
            }
            case "dialog" -> {
                LoadingDialog.showLoading(requireActivity());

                mHandler.postDelayed(() -> {
                    LoadingDialog.hide();

                    LoadingDialog.Options options = new LoadingDialog.Options();
                    options.cancelable = true;
                    LoadingDialog.showLoading(requireActivity(), options);
                }, 3000);
            }
            case "snackbar" -> showSnackbar(view);
            case "webview" -> startActivity(new Intent(requireActivity(), WebActivity.class));
            case "database" -> startActivity(new Intent(requireActivity(), DataBaseActivity.class));
            case "compose" -> startActivity(new Intent(requireActivity(), ComposeActivity.class));
            case "http" -> startActivity(new Intent(requireActivity(), HttpActivity.class));
            case "ktx" -> startActivity(new Intent(requireActivity(), KotlinActivity.class));
            case "animation" ->
                    startActivity(new Intent(requireActivity(), AnimationActivity.class));
            case "dataBinding" ->
                    startActivity(new Intent(requireActivity(), DataBindingActivity.class));
            case "edit" -> startActivity(new Intent(requireActivity(), EditActivity.class));
            case "rxJava" -> startActivity(new Intent(requireActivity(), RxJavaActivity.class));
            case "paging" -> startActivity(new Intent(requireActivity(), Paging3Activity.class));
            case "bluetooth" ->
                    startActivity(new Intent(requireActivity(), BluetoothActivity.class));
            case "bluetoothLe" ->
                    startActivity(new Intent(requireActivity(), BluetoothLeActivity.class));
            case "audio" -> startActivity(new Intent(requireActivity(), AudioActivity.class));
            case "camera" -> startActivity(new Intent(requireActivity(), CameraActivity.class));
            case "camera2" -> startActivity(new Intent(requireActivity(), Camera2Activity.class));
            case "camerax" ->
                    startActivity(new Intent(requireActivity(), TakePictureActivity.class));
            case "video" -> startActivity(new Intent(requireActivity(), TakeVideoActivity.class));
        }
    }

    private void showSnackbar(View v) {
        Timber.d("showSnackbar ");
        Snackbar.make(v, "Snackbar", Snackbar.LENGTH_SHORT)
                // .setBackgroundTint(requireContext().getColor(R.color.white))
                // .setTextColor(requireContext().getColor(R.color.deep_purple_100))
                .setAction("Ok", v1 -> showToast("Snackbar Ok"))
                .setAnchorView(v)
                .show();
    }

    private void logBuildInfo() {
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
        new AlertDialog.Builder(requireContext())
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
        ConnectivityManager manager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Timber.d("showInternet: isDefaultNetworkActive=" + manager.isDefaultNetworkActive());
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
            Timber.d("showInternet: SDK_INT < Build.VERSION_CODES.M");
        }
    }

    void showDisplay() {
        Timber.d("--------------------------------------------------------");
        WindowManager wm = ContextCompat.getSystemService(requireContext(), WindowManager.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        Timber.d("getMetrics: " + displayMetrics);
        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        Timber.d("getRealMetrics: " + displayMetrics);
    }
}

