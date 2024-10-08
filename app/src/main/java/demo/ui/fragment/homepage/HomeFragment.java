package demo.ui.fragment.homepage;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Field;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidz.LoadingDialog;
import composex.ui.ComposeActivity;
import dagger.hilt.android.AndroidEntryPoint;
import demo.databinding.ActivityHomeBinding;
import demo.databinding.ItemHomeFunBinding;
import demo.di.MainHandler;
import demo.ui.activity.AnimationActivity;
import demo.ui.activity.DataBaseActivity;
import demo.ui.activity.RxJavaActivity;
import demo.ui.activity.WebSocketActivity;
import demo.ui.adapter.BaseAdapter;
import demo.ui.adapter.BindingViewHolder;
import demo.ui.base.BaseFragment;
import demo.ui.databinding.DataBindingActivity;
import demo.ui.edit.EditActivity;
import demo.ui.http.HttpActivity;
import demo.ui.ktx.KotlinActivity;
import demo.ui.paging.Paging3Activity;
import demo.util.KeyboardWatcher;
import demo.web.WebActivity;
import timber.log.Timber;

import static android.content.Context.CONNECTIVITY_SERVICE;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment {
    private static final String TAG = "HomeFragment";
    private static final String execute = "execute";
    private static final String popup = "popup";
    private static final String snackbar = "snackbar";
    private static final String webview = "webview";
    private static final String database = "database";
    private static final String dialog = "dialog";
    private static final String compose = "compose";
    private static final String http = "http";
    private static final String ktx = "ktx";
    private static final String animation = "animation";
    private static final String dataBinding = "dataBinding";
    private static final String edit = "edit";
    private static final String rxJava = "rxJava";
    private static final String paging = "paging";
    private static final String webSocket = "webSocket";

    @Inject
    MainHandler mHandler;
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

        String[] strings = new String[]{
                execute, snackbar, popup, database, dialog, compose,
                animation, dataBinding, edit, http, ktx, rxJava, webview, paging, webSocket
        };
        binding.recyclerview.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));
        binding.recyclerview.setAdapter(new BaseAdapter<String, BindingViewHolder<ItemHomeFunBinding>>(strings) {

            @NonNull
            @Override
            public BindingViewHolder<ItemHomeFunBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                return new BindingViewHolder<>(ItemHomeFunBinding.inflate(inflater, parent, false));
            }

            @Override
            public void onBindViewHolder(@NonNull BindingViewHolder<ItemHomeFunBinding> holder, int position) {
                String cmd = getItem(position);
                TextView textView = holder.binding.execute;
                textView.setText(cmd);
                textView.setOnClickListener(v -> {
                    switch (cmd) {
                        case execute -> {
                            Timber.d("execute");
                            textView.setText(android.R.string.ok);
                            textView.setText(androidz.R.string.loading_message);
                            logBuildInfo();
                        }
                        case snackbar -> {
                            showSnackbar(textView);
                        }
                        case popup -> {
                            if (popupWindow == null) {
                                popupWindow = new HomePopupWindow(requireActivity());
                            }
                            popupWindow.showPopupWindow(holder.itemView);
                            // popupWindow.showPopupWindow(100, 100);
                        }
                        case webview -> {
                            startActivity(new Intent(requireActivity(), WebActivity.class));
                        }
                        case database -> {
                            startActivity(new Intent(requireActivity(), DataBaseActivity.class));
                        }
                        case dialog -> {
                            LoadingDialog.showLoading(requireActivity());
                        }
                        case compose -> {
                            startActivity(new Intent(requireActivity(), ComposeActivity.class));
                        }
                        case http -> {
                            startActivity(new Intent(requireActivity(), HttpActivity.class));
                        }
                        case ktx -> {
                            startActivity(new Intent(requireActivity(), KotlinActivity.class));
                        }
                        case animation -> {
                            startActivity(new Intent(requireActivity(), AnimationActivity.class));
                        }
                        case dataBinding -> {
                            startActivity(new Intent(requireActivity(), DataBindingActivity.class));
                        }
                        case edit -> {
                            startActivity(new Intent(requireActivity(), EditActivity.class));
                        }
                        case rxJava -> {
                            startActivity(new Intent(requireActivity(), RxJavaActivity.class));
                        }
                        case paging -> {
                            startActivity(new Intent(requireActivity(), Paging3Activity.class));
                        }
                        case webSocket -> {
                            startActivity(new Intent(requireActivity(), WebSocketActivity.class));
                        }
                    }
                });
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
        Log.d(TAG, "openDialog");
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

    void registerDefaultNetworkCallback() {
        ConnectivityManager c = (ConnectivityManager) requireContext().getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                    Log.d(TAG, "onAvailable: network=" + network);
                }

                @Override
                public void onLosing(@NonNull Network network, int maxMsToLive) {
                    super.onLosing(network, maxMsToLive);
                    Log.d(TAG, "onLosing: maxMsToLive=" + maxMsToLive);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Log.d(TAG, "onLost: network=" + network);
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Log.d(TAG, "onUnavailable: ");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    Log.d(TAG, "onCapabilitiesChanged: network=" + network);
                    Log.d(TAG, "onCapabilitiesChanged: networkCapabilities=" + networkCapabilities);
                }

                @Override
                public void onLinkPropertiesChanged(@NonNull Network network, @NonNull LinkProperties linkProperties) {
                    super.onLinkPropertiesChanged(network, linkProperties);
                    Log.d(TAG, "onLinkPropertiesChanged: linkProperties=" + linkProperties);
                }

                @Override
                public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
                    super.onBlockedStatusChanged(network, blocked);
                    Log.d(TAG, "onBlockedStatusChanged: blocked=" + blocked);
                }
            });
        }
    }

    void showInternet() {
        ConnectivityManager c = (ConnectivityManager) requireContext().getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities n = c.getNetworkCapabilities(c.getActiveNetwork());
            Log.d(TAG, "showInternet: getActiveNetwork=" + c.getActiveNetwork());
            if (n == null) {
                return;
            }
            Log.d(TAG, "showInternet: isDefaultNetworkActive=" + c.isDefaultNetworkActive());
            Log.d(TAG, "showInternet: isActiveNetworkMetered=" + c.isActiveNetworkMetered());
            Log.d(TAG, "showInternet: getNetworkCapabilities=" + n);
            Log.d(TAG, "showInternet: getLinkUpstreamBandwidthKbps=" + n.getLinkUpstreamBandwidthKbps());
            Log.d(TAG, "showInternet: getLinkDownstreamBandwidthKbps=" + n.getLinkDownstreamBandwidthKbps());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d(TAG, "showInternet: getSignalStrength=" + n.getSignalStrength());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                Log.d(TAG, "showInternet: getTransportInfo=" + n.getTransportInfo());
            }

            boolean connect = n.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || n.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || n.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            boolean internet = n.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            Log.d(TAG, "showInternet: connect=" + connect + ", internet=" + internet);

            if (n.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "hasTransport(NetworkCapabilities.TRANSPORT_WIFI)");
            }
            if (n.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)");
            }
            if (n.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d(TAG, "hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)");
            }
            if (n.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                Log.d(TAG, "hasTransport(NetworkCapabilities.TRANSPORT_VPN)");
            }

            if (n.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                Log.d(TAG, "hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)");
            }
            if (n.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)) {
                Log.d(TAG, "hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)");
            }
            if (n.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                Log.d(TAG, "hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)");
            }
        } else {
            Log.d(TAG, "showInternet: SDK_INT < Build.VERSION_CODES.M");
        }
    }

    void showDisplay() {
        Log.d(TAG, "--------------------------------------------------------");
        WindowManager wm = ContextCompat.getSystemService(requireContext(), WindowManager.class);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        Log.d(TAG, "displayMetrics: " + displayMetrics);
        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        Log.d(TAG, "getRealMetrics: " + displayMetrics);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d(TAG, "--------------------------------------------------------");
            Log.d(TAG, "getCurrentWindowMetrics: " + wm.getCurrentWindowMetrics().getBounds());
        }
    }

}

