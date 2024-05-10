package androidz

import android.app.Application
import android.content.ComponentCallbacks
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager.PackageInfoFlags
import android.os.Build
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * Delegate application
 * @see Androidz
 */
object App : Application(), ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    private lateinit var app: Application

    fun attachApplication(application: Application) {
        app = application
        attachBaseContext(application.baseContext)
    }

    @JvmField
    val globalData: MutableMap<String, Any> = mutableMapOf()

    val isDebuggable: Boolean by lazy {
        applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE > 0
    }

    val packageInfo: PackageInfo by lazy {
        val info = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }
        // consistent with app
        info.applicationInfo = applicationInfo
        return@lazy info
    }

    override val viewModelStore: ViewModelStore by lazy {
        ViewModelStore()
    }

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = ViewModelProvider.AndroidViewModelFactory.getInstance(app)

    /**
     * use registerComponentCallbacks instead of
     * @see onConfigurationChanged
     * @see onLowMemory
     * @see onTrimMemory
     * @see ComponentCallbacks
     */
    override fun registerComponentCallbacks(callback: ComponentCallbacks) {
        app.registerComponentCallbacks(callback)
    }

    override fun unregisterComponentCallbacks(callback: ComponentCallbacks) {
        app.unregisterComponentCallbacks(callback)
    }

    override fun registerActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        app.registerActivityLifecycleCallbacks(callback)
    }

    override fun unregisterActivityLifecycleCallbacks(callback: ActivityLifecycleCallbacks) {
        app.unregisterActivityLifecycleCallbacks(callback)
    }

    override fun registerOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        app.registerOnProvideAssistDataListener(callback)
    }

    override fun unregisterOnProvideAssistDataListener(callback: OnProvideAssistDataListener) {
        app.unregisterOnProvideAssistDataListener(callback)
    }

    override fun equals(other: Any?): Boolean {
        return app == other
    }

    override fun hashCode(): Int {
        return app.hashCode()
    }

    override fun toString(): String {
        return app.toString()
    }
}