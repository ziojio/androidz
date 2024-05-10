package androidz

import android.content.Context
import androidx.startup.Initializer


class AndroidzInitializer : Initializer<AndroidzInitializer> {

    override fun create(context: Context): AndroidzInitializer {
        Androidz.initialize(context)
        return this
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
