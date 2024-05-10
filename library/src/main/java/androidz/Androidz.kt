package androidz

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Main
 */
object Androidz {

    @JvmStatic
    fun initialize(context: Context) {
        App.attachApplication(context.applicationContext as Application)
        isDebuggable = App.isDebuggable
        ActivityStackManager.register(app)
        ActivityStackManager.isDebuggable = isDebuggable
    }

    @JvmStatic
    val app: App = App

    @JvmStatic
    val handler: Handler = Handler(Looper.getMainLooper())

    @JvmStatic
    var isDebuggable: Boolean = false

    @JvmStatic
    var logger: Logger = object : Logger() {
        override fun log(priority: Int, tag: String, msg: String, tr: Throwable?) {
            if (!isDebuggable)
                return
            when (priority) {
                Log.VERBOSE -> Log.v(tag, msg, tr)
                Log.DEBUG -> Log.d(tag, msg, tr)
                Log.INFO -> Log.i(tag, msg, tr)
                Log.WARN -> Log.w(tag, msg, tr)
                Log.ERROR -> Log.e(tag, msg, tr)
                else -> Log.v(tag, msg, tr)
            }
        }
    }

    @JvmStatic
    val isMainThread: Boolean
        get() = Looper.getMainLooper().thread === Thread.currentThread()

    @JvmStatic
    val isBackgroundThread: Boolean
        get() = !isMainThread

    @JvmStatic
    @JvmOverloads
    fun runOnUiThread(r: Runnable, delayMillis: Long = 0) {
        handler.postDelayed(r, delayMillis)
    }

}
