package androidz.util;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class NetworkUtil {

    public static void register(@NonNull Context context, @NonNull ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest.Builder builder = new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            manager.registerNetworkCallback(request, networkCallback);
        }
    }

    public static void unregister(@NonNull Context context, @NonNull ConnectivityManager.NetworkCallback networkCallback) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        if (manager != null) {
            manager.unregisterNetworkCallback(networkCallback);
        }
    }

    /**
     * Open the settings of wireless.
     */
    public static void openWirelessSettings(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Return whether network is connected.
     */
    public static boolean isConnected(@NonNull Context context) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return false;
            }
            return networkInfo.isConnected();
        }
    }

    /**
     * Return whether network is metered.
     */
    public static boolean isMeteredNetwork(@NonNull Context context) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        return manager.isActiveNetworkMetered();
    }

    /**
     * Return whether internet is need login.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isCaptivePortalNetwork(@NonNull Context context) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        Network network = manager.getActiveNetwork();
        if (network == null) {
            return false;
        }
        NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
        if (capabilities == null) {
            return false;
        }
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL);
    }

    /**
     * Return whether using mobile data.
     */
    public static boolean isMobile(@NonNull Context context) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return false;
            }
            return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected();
        }
    }

    /**
     * Return whether wifi is connected.
     */
    public static boolean isWifi(@NonNull Context context) {
        ConnectivityManager manager = getSystemService(context, ConnectivityManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(network);
            if (capabilities == null) {
                return false;
            }
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo == null) {
                return false;
            }
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected();
        }
    }

}
