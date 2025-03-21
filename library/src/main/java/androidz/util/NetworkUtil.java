package androidz.util;

import static androidx.core.content.ContextCompat.getSystemService;
import static java.util.Objects.requireNonNull;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;


public final class NetworkUtil {

    @NonNull
    public static ConnectivityManager getConnectivityManager() {
        return requireNonNull(getSystemService(Androidz.getContext(), ConnectivityManager.class));
    }

    /**
     * 网络是否可用
     */
    public static boolean isAvailable() {
        ConnectivityManager manager = getConnectivityManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
                }
            }
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                //noinspection deprecation
                return networkInfo.isConnected();
            }
        }
        return false;
    }

    /**
     * 网络按流量计费
     */
    public static boolean isMeteredNetwork() {
        return getConnectivityManager().isActiveNetworkMetered();
    }

    /**
     * 网络具有强制门户（需要登陆）
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean isCaptivePortal() {
        ConnectivityManager manager = getConnectivityManager();
        Network network = manager.getActiveNetwork();
        if (network != null) {
            NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL);
            }
        }
        return false;
    }

    public static boolean isMobile() {
        ConnectivityManager manager = getConnectivityManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            }
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
            }
        }
        return false;
    }

    public static boolean isWifi() {
        ConnectivityManager manager = getConnectivityManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
                }
            }
        } else {
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
            }
        }
        return false;
    }
}
