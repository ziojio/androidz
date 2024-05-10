package androidz

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService

object ClipboardUtil {

    /**
     * Copy the text to clipboard.
     */
    @JvmStatic
    fun copyText(context: Context, text: CharSequence?) {
        val cm = context.getSystemService<ClipboardManager>()
        cm?.setPrimaryClip(ClipData.newPlainText(context.packageName, text))
    }

    /**
     * Copy the text to clipboard.
     *
     * @param label The label.
     * @param text  The text.
     */
    @JvmStatic
    fun copyText(context: Context, label: CharSequence?, text: CharSequence?) {
        val cm = context.getSystemService<ClipboardManager>()
        cm?.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    /**
     * Clear the clipboard.
     */
    @JvmStatic
    fun clear(context: Context) {
        val cm = context.getSystemService<ClipboardManager>()
        cm?.setPrimaryClip(ClipData.newPlainText(null, ""))
    }

    /**
     * @return the label for clipboard
     */
    @JvmStatic
    fun getLabel(context: Context): CharSequence {
        val cm = context.getSystemService<ClipboardManager>()
        return cm?.primaryClipDescription?.label ?: return ""
    }

    /**
     * @return the text for clipboard
     */
    @JvmStatic
    fun getText(context: Context): CharSequence {
        val cm = context.getSystemService<ClipboardManager>()
        val clip = cm?.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).coerceToText(context)
            if (text != null) {
                return text
            }
        }
        return ""
    }
}