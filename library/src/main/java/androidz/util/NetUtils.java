package androidz.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.InetAddresses;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;


public final class NetUtils {

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) UtilApp.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager manager = getConnectivityManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network network = manager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = manager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
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
     * 网络是否适合传输大量数据
     */
    public static boolean isNetworkMetered() {
        return getConnectivityManager().isActiveNetworkMetered();
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

    /**
     * 是否为IPv4 0.0.0.0 -> 255.255.255.255
     */
    public static boolean isIPv4(String ip) {
        if (!ip.contains(".")) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return InetAddresses.isNumericAddress(ip);
        }
        return ip.matches("^([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3}$");
    }

    /**
     * 是否为IPv6 :: -> FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF
     */
    public static boolean isIPv6(String ip) {
        if (!ip.contains(":")) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return InetAddresses.isNumericAddress(ip);
        }
        return ip.matches("^::"
                + "|:(:[0-9a-fA-F]{1,4}){1,7}"
                + "|([0-9a-fA-F]{1,4}:){1,7}:"
                + "|[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}"
                + "|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}"
                + "|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}"
                + "|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}"
                + "|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}"
                + "|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}"
                + "|[0-9a-fA-F]{1,4}:(:[0-9a-fA-F]{1,4}){1,6}$");
    }
}
