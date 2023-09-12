package demo.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidz.app.AppActivity;
import androidz.app.LoadingDialog;
import androidz.app.LoadingDialogFragment;
import androidz.util.OnDebouncingClickListener;
import androidz.util.ToastUtil;
import demo.R;
import demo.databinding.ActivityMainBinding;

public class MainActivity extends AppActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.execFunction.setOnClickListener((OnDebouncingClickListener) v -> {
            Log.d(TAG, "execFunction");
        });

        binding.showFunction.setOnClickListener(v -> {
            Log.d(TAG, "showFunction");

            showDisplay();
        });
        binding.openDialog.setOnClickListener(v -> {
            Log.d(TAG, "openDialog");

            new AlertDialog.Builder(this, 0)
                    .setTitle("Title")
                    .setMessage("AlertDialog")
                    .setCancelable(true)
                    .setPositiveButton("a", (dialog12, which) -> {
                        new LoadingDialog(this).show();
                    })
                    .setNegativeButton("b", (dialog1, which) -> {
                        new LoadingDialogFragment().show(getSupportFragmentManager(), null);
                    })
                    .setOnCancelListener(dialog -> {

                    })
                    .show();
        });
        binding.openUrl.setOnClickListener(v -> {
            Log.d(TAG, "openUrl");

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://hummer.didi.cn/assets/test.html"));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                ToastUtil.showToast(e.getMessage());
            }
        });
        binding.network.setOnClickListener(v -> {
            Log.d(TAG, "network");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (checkSelfPermission(Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1);
                    return;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                    return;
                } else if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
                    return;
                }
            }
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d(TAG, "wifiInfo: " + wifiInfo);
            Log.d(TAG, "IpAddress: " + Formatter.formatIpAddress(wifiInfo.getIpAddress()));
            // Log.d(TAG, "getWifiMacAddress: " + getWifiMacAddress());
            Log.d(TAG, "getMacFromHardware: " + getMacFromHardware());
            Log.d(TAG, "MacAddress: " + wifiInfo.getMacAddress());

            Log.d(TAG, "wifiName: " + wifiInfo.getSSID());
            Log.d(TAG, "wifiMac: " + wifiInfo.getBSSID());
        });

        MAdapter adapter = new MAdapter(this);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: " + position);
                Log.d(TAG, "getCheckedItemPosition: " + binding.listView.getCheckedItemPosition());
                Log.d(TAG, "getCheckedItemPositions: " + binding.listView.getCheckedItemPositions());
                ToastUtil.showToast("onItemClick position " + position);
            }
        });
        binding.listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected");
            }
        });
        binding.listView.setVisibility(View.GONE);
    }

    public static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0"))
                    continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) return "";
                StringBuilder res1 = new StringBuilder();
                for (Byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }
                if (!TextUtils.isEmpty(res1)) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getWifiMacAddress() {
        String mac = "";
        try {
            InputStream inputStream = new FileInputStream("/sys/class/net/wlan0/address");
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(reader);
            mac = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }

    void regInternet() {
        ConnectivityManager c = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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
        ConnectivityManager c = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
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
        WindowManager wm = ContextCompat.getSystemService(this, WindowManager.class);
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

    static class MAdapter extends ArrayAdapter<String> {
        final String[] strings = {"Aaaa", "Bbbb", "Cccc"};

        public MAdapter(@NonNull Context context) {
            super(context, R.layout.item_selected2);
            addAll(strings);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }

}
