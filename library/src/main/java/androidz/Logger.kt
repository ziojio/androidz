package androidz

import android.util.Log


abstract class Logger {

    @JvmOverloads
    fun v(tag: String, msg: String, tr: Throwable? = null) {
        log(Log.VERBOSE, tag, msg, tr)
    }

    @JvmOverloads
    fun d(tag: String, msg: String, tr: Throwable? = null) {
        log(Log.DEBUG, tag, msg, tr)
    }

    @JvmOverloads
    fun i(tag: String, msg: String, tr: Throwable? = null) {
        log(Log.INFO, tag, msg, tr)
    }

    @JvmOverloads
    fun w(tag: String, msg: String, tr: Throwable? = null) {
        log(Log.WARN, tag, msg, tr)
    }

    @JvmOverloads
    fun e(tag: String, msg: String, tr: Throwable? = null) {
        log(Log.ERROR, tag, msg, tr)
    }

    abstract fun log(priority: Int, tag: String, msg: String, tr: Throwable?)
}
