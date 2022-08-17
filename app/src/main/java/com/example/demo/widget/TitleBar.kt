package com.example.demo.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.demo.R

/**
 * 标题栏
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
    val left: TextView

    @JvmField
    val right: TextView

    @JvmField
    val line: View

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.TitleBar, defStyleAttr, defStyleRes)
        val titleText = a.getString(R.styleable.TitleBar_title)
        val leftVisible = a.getBoolean(R.styleable.TitleBar_leftVisible, true)
        val leftIcon = a.getDrawable(R.styleable.TitleBar_leftIcon)
            ?: ContextCompat.getDrawable(context, R.drawable.ic_back)
        val leftText = a.getString(R.styleable.TitleBar_leftText)
        val rightIcon = a.getDrawable(R.styleable.TitleBar_rightIcon)
        val rightText = a.getString(R.styleable.TitleBar_rightText)
        val lineVisible = a.getBoolean(R.styleable.TitleBar_lineVisible, true)
        val lineSize = a.getDimensionPixelSize(R.styleable.TitleBar_lineSize, 1)
        val lineColor = a.getColor(
            R.styleable.TitleBar_lineColor,
            ContextCompat.getColor(context, R.color.divider_color)
        )
        a.recycle()

        val density = context.resources.displayMetrics.density
        title = TextView(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                val margin = (64 * density).toInt()
                marginStart = margin
                marginEnd = margin
                gravity = Gravity.CENTER
            }
            text = titleText
        }
        left = TextView(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                val margin = (16 * density).toInt()
                marginStart = margin
                gravity = Gravity.CENTER_VERTICAL or Gravity.START
            }
            leftIcon?.apply {
                val width = (24 * density).toInt()
                setBounds(0, 0, width, width)
                setCompoundDrawablesRelative(this, null, null, null)
            }
            leftText?.apply {
                text = this
            }
            gravity = Gravity.CENTER_VERTICAL
            visibility = if (leftVisible) VISIBLE else GONE
        }
        right = TextView(context).apply {
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                val margin = (16 * density).toInt()
                marginEnd = margin
                gravity = Gravity.CENTER_VERTICAL or Gravity.END
            }
            rightIcon?.apply {
                val width = (24 * density).toInt()
                setBounds(0, 0, width, width)
                setCompoundDrawablesRelative(null, null, this, null)
            }
            rightText?.apply {
                text = this
            }
            gravity = Gravity.CENTER_VERTICAL
            visibility = if (rightIcon != null || rightText != null) VISIBLE else GONE
        }
        line = View(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, lineSize).apply {
                gravity = Gravity.BOTTOM
                setBackgroundColor(lineColor)
            }
            visibility = if (lineVisible) VISIBLE else GONE
        }

        left.setOnClickListener {
            if (context is FragmentActivity) {
                val manager = context.supportFragmentManager
                if (manager.backStackEntryCount > 0) {
                    manager.popBackStack()
                } else {
                    context.finish()
                }
            } else if (context is Activity) {
                context.finish()
            }
        }

        addView(title)
        addView(left)
        addView(right)
        addView(line)
    }
}