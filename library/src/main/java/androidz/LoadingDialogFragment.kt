package androidz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment

/**
 * 屏幕旋转时保持状态
 */
class LoadingDialogFragment : AppCompatDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentId = arguments?.getInt(LOADING_LAYOUT_RES, R.layout.loading_dialog)
            ?: R.layout.loading_dialog
        return inflater.inflate(contentId, container ?: FrameLayout(requireActivity()), false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val message = arguments?.getCharSequence(LOADING_MESSAGE)
        if (message != null) {
            val textView = view.findViewById<TextView>(R.id.loading_message)
            textView?.text = message
        }
    }

    companion object {
        private const val LOADING_LAYOUT_RES = "loading_layout_res"
        private const val LOADING_MESSAGE = "loading_message"

        @JvmStatic
        @JvmOverloads
        fun newInstance(
            message: CharSequence? = null,
            @LayoutRes contentId: Int = 0
        ): LoadingDialogFragment {
            val args = Bundle()
            if (contentId != 0) args.putInt(LOADING_LAYOUT_RES, contentId)
            if (message != null) args.putCharSequence(LOADING_MESSAGE, message)
            val fragment = LoadingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}