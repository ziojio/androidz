package androidz.app

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialog
import com.ziojio.androidz.R


class LoadingDialog @JvmOverloads constructor(
    context: Context,
    val message: CharSequence? = null,
    @LayoutRes
    val contentId: Int = R.layout.loading_dialog
) : AppCompatDialog(context) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        if (message != null) {
            val textView = findViewById<TextView>(R.id.loading_message)
            textView?.text = message
        }
    }
}