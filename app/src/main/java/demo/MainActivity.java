package demo;

import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidz.app.AppActivity;
import androidz.util.OnDebouncingClickListener;
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
            showDisplay();
        });
        binding.openDialog.setOnClickListener(v -> {
            Log.d(TAG, "openDialog");
            new AlertDialog.Builder(this)
                    .setTitle("AlertDialog")
                    .setMessage("This is AlertDialog")
                    .setCancelable(true)
                    .setPositiveButton("LoadingDialog", (dialog12, which) -> {
                        new ExampleDialog(this).show();
                    })
                    .setNegativeButton("LoadingDialogFragment", (dialog1, which) -> {
                        new ExampleDialogFragment().show(getSupportFragmentManager(), null);
                    })
                    .show();
        });
        binding.network.setOnClickListener(v -> {
            Log.d(TAG, "network");
        });
    }

    void registerDefaultNetworkCallback() {
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

}
