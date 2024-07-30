package demo.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import demo.R

/**
 * 标题栏
 *
 * 默认注册了返回的监听, 关闭 Activity 或 Fragment
 */
class TitleBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @JvmField
    val title: TextView

    @JvmField
    val left: ImageView

    @JvmField
    val right: TextView

    @JvmField
    val line: View


    init {
        LayoutInflater.from(context).inflate(R.layout.titlebar, this, true)
        title = findViewById(R.id.title)
        left = findViewById(R.id.left)
        right = findViewById(R.id.right)
        line = findViewById(R.id.line)

        initialize(context, attrs, defStyleAttr, defStyleRes)

        left.setOnClickListener {
            if (getContext() is FragmentActivity) {
                val activity = getContext() as FragmentActivity
                val manager = activity.supportFragmentManager
                if (manager.backStackEntryCount > 0) {
                    manager.popBackStack()
                } else {
                    activity.finish()
                }
            } else if (getContext() is Activity) {
                val activity = getContext() as FragmentActivity
                activity.finish()
            }
        }
    }

    private fun initialize(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, defStyleRes)
        val titleText = a.getString(R.styleable.TitleBar_title)
        val leftIcon = a.getDrawable(R.styleable.TitleBar_leftIcon)
        val rightIcon = a.getDrawable(R.styleable.TitleBar_rightIcon)
        val rightText = a.getString(R.styleable.TitleBar_rightText)
        val leftVisible = a.getBoolean(R.styleable.TitleBar_leftVisible, true)
        val lineVisible = a.getBoolean(R.styleable.TitleBar_lineVisible, true)
        a.recycle()

        if (titleText != null) {
            title.text = titleText
        }
        if (leftIcon != null) {
            left.setImageDrawable(leftIcon)
        }
        if (rightText != null) {
            right.text = rightText
            right.visibility = VISIBLE
        }
        if (rightIcon != null) {
            right.setCompoundDrawablesRelative(rightIcon, null, null, null)
            right.visibility = VISIBLE
        }
        left.visibility = if (leftVisible) VISIBLE else GONE
        line.visibility = if (lineVisible) VISIBLE else GONE
    }

}