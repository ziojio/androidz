package androidz.util

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
    }

    @JvmStatic
    val app: App = App

    @JvmStatic
    val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

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
    fun isMainThread(): Boolean {
        return Looper.getMainLooper().thread === Thread.currentThread()
    }

    @JvmStatic
    fun isBackgroundThread(): Boolean {
        return !isMainThread()
    }

    @JvmStatic
    fun runOnUiThread(runnable: Runnable) {
        if (isMainThread()) {
            runnable.run()
        } else {
            handler.post(runnable)
        }
    }

}
