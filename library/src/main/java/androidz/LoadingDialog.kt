package androidz

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDialog


class LoadingDialog @JvmOverloads constructor(
    context: Context,
    private val message: CharSequence? = null,
    @LayoutRes
    private val contentId: Int = 0,
    @StyleRes
    private val themeId: Int = 0
) : AppCompatDialog(context, themeId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(if (contentId == 0) R.layout.loading_dialog else contentId)
        if (message != null) {
            val textView = findViewById<TextView>(R.id.loading_message)
            textView?.text = message
        }
    }
}