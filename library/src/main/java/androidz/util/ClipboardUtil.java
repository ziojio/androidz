package androidz.util;

import static androidx.core.content.ContextCompat.getSystemService;
import static java.util.Objects.requireNonNull;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;

import androidx.annotation.NonNull;

public class ClipboardUtil {

    /**
     * Copy the text to clipboard.
     */
    public static void copyText(@NonNull Context context, CharSequence text) {
        ClipboardManager cm = requireNonNull(getSystemService(context, ClipboardManager.class));
        cm.setPrimaryClip(ClipData.newPlainText(context.getPackageName(), text));
    }

    /**
     * Copy the text to clipboard.
     *
     * @param label The label.
     * @param text  The text.
     */
    public static void copyText(@NonNull Context context, CharSequence label, CharSequence text) {
        ClipboardManager cm = requireNonNull(getSystemService(context, ClipboardManager.class));
        cm.setPrimaryClip(ClipData.newPlainText(label, text));
    }

    /**
     * Clear the clipboard.
     */
    public static void clear(@NonNull Context context) {
        ClipboardManager cm = requireNonNull(getSystemService(context, ClipboardManager.class));
        cm.setPrimaryClip(ClipData.newPlainText(null, ""));
    }

    /**
     * @return the label for clipboard
     */
    public static CharSequence getLabel(@NonNull Context context) {
        ClipboardManager cm = requireNonNull(getSystemService(context, ClipboardManager.class));
        ClipDescription des = cm.getPrimaryClipDescription();
        if (des == null) {
            return "";
        }
        CharSequence label = des.getLabel();
        if (label == null) {
            return "";
        }
        return label;
    }

    /**
     * @return the text for clipboard
     */
    public static CharSequence getText(@NonNull Context context) {
        ClipboardManager cm = requireNonNull(getSystemService(context, ClipboardManager.class));
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            CharSequence text = clip.getItemAt(0).coerceToText(context);
            if (text != null) {
                return text;
            }
        }
        return "";
    }
}