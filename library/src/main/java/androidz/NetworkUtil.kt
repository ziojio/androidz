package androidz

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService


object NetworkUtil {

    @JvmStatic
    fun register(context: Context, networkCallback: NetworkCallback) {
        val manager = context.getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            val builder = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val request = builder.build()
            manager?.registerNetworkCallback(request, networkCallback)
        }
    }

    @JvmStatic
    fun unregister(context: Context, networkCallback: NetworkCallback) {
        val manager = context.getSystemService<ConnectivityManager>()
        manager?.unregisterNetworkCallback(networkCallback)
    }

    /**
     * Open the settings of wireless.
     */
    @JvmStatic
    fun openWirelessSettings(context: Context) {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * Return whether network is connected.
     */
    @JvmStatic
    fun isAvailable(context: Context): Boolean {
        val manager = context.getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager?.activeNetwork ?: return false
            val capabilities = manager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val networkInfo = manager?.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    /**
     * Return whether network is metered.
     */
    @JvmStatic
    fun isMeteredNetwork(context: Context): Boolean {
        val manager = context.getSystemService<ConnectivityManager>()
        return manager?.isActiveNetworkMetered ?: false
    }

    /**
     * Return whether internet is need login.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @JvmStatic
    fun isCaptivePortalNetwork(context: Context): Boolean {
        val manager = context.getSystemService<ConnectivityManager>()
        val network = manager?.activeNetwork ?: return false
        val capabilities = manager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_CAPTIVE_PORTAL)
    }

    /**
     * Return whether using mobile data.
     */
    @JvmStatic
    fun isMobile(context: Context): Boolean {
        val manager = context.getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager?.activeNetwork ?: return false
            val capabilities = manager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = manager?.activeNetworkInfo ?: return false
            return networkInfo.type == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnected
        }
    }

    /**
     * Return whether wifi is connected.
     */
    @JvmStatic
    fun isWifi(context: Context): Boolean {
        val manager = context.getSystemService<ConnectivityManager>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager?.activeNetwork ?: return false
            val capabilities = manager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = manager?.activeNetworkInfo ?: return false
            return networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        }
    }
}
