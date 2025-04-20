package androidz.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;

@SuppressWarnings("unused")
public final class NetUtils {

    private static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) UtilApp.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
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
        if (ip == null || ip.length() < 7 || ip.length() > 15) {
            return false;
        }
        return ip.matches("^([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])){3}$");
    }

    /**
     * 是否为IPv6 :: -> FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF
     */
    public static boolean isIPv6(String ip) {
        if (ip == null || ip.length() < 2 || ip.length() > 39) {
            return false;
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

    /**
     * 是否为子网掩码 128.0.0.0/1 -> 255.255.255.254/31
     */
    public static boolean isSubnetMask(String mask) {
        if (mask == null || mask.length() < 9 || mask.length() > 15) {
            return false;
        }
        if (mask.matches("^(128|192|224|240|248|252|254|255)\\.((0|128|192|224|240|248|252|254|255)\\.){2}(0|128|192|224|240|248|252|254)$")) {
            StringBuilder builder = new StringBuilder(32);
            String[] split = mask.split("\\.");
            for (String str : split) {
                builder.append(Integer.toBinaryString(Integer.parseInt(str)));
            }
            return builder.indexOf("01") == -1; // 掩码前缀必须是连续的1，不能存在01
        }
        return false;
    }

    @NonNull
    public static String getSubnetMask(int prefixLength) {
        if (prefixLength < 0 || prefixLength > 32) {
            throw new IllegalArgumentException("Invalid prefix length (0 <= prefix <= 32)");
        }
        int prefix = 0xffffffff << (32 - prefixLength);
        int[] mask = new int[4];
        mask[0] = prefix >> 24 & 0xff;
        mask[1] = prefix >> 16 & 0xff;
        mask[2] = prefix >> 8 & 0xff;
        mask[3] = prefix & 0xff;
        return mask[0] + "." + mask[1] + "." + mask[2] + "." + mask[3];
    }

    public static int subnetMaskToPrefixLength(String mask) {
        if (!isSubnetMask(mask)) {
            throw new IllegalArgumentException("Invalid subnet mask " + mask);
        }
        int prefixLength = 0;
        String[] strings = mask.split("\\.");
        for (String str : strings) {
            int bitCount = Integer.bitCount(Integer.parseInt(str));
            prefixLength += bitCount;
        }
        return prefixLength;
    }
}
