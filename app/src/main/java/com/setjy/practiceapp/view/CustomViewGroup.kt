package com.setjy.practiceapp.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.setjy.practiceapp.R
import com.setjy.practiceapp.databinding.CustomViewGroupBinding

class CustomViewGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrs: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttrs, defStyleRes) {

    val binding: CustomViewGroupBinding

    private val avatarRect = Rect()
    private val usernameRect = Rect()
    private val messageRect = Rect()
    private val flexboxRect = Rect()
    private val backgroundRect = Rect()

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_view_group, this, true)
        binding = CustomViewGroupBinding.bind(this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var usedWidth = 0
        var usedHeight = 0

        measureChildWithMargins(
            binding.ivAvatar,
            widthMeasureSpec,
            usedWidth,
            heightMeasureSpec,
            usedHeight
        )
        usedWidth += getWidthWithMargins(binding.ivAvatar)
        measureChildWithMargins(
            binding.cvBackground,
            widthMeasureSpec,
            usedWidth,
            heightMeasureSpec,
            usedHeight
        )
        measureChildWithMargins(
            binding.tvUsername,
            widthMeasureSpec,
            usedWidth,
            heightMeasureSpec,
            usedHeight
        )
        usedHeight += getHeightWithMargins(binding.tvUsername)
        measureChildWithMargins(
            binding.tvMessage,
            widthMeasureSpec,
            usedWidth,
            heightMeasureSpec,
            usedHeight
        )
        usedHeight += getHeightWithMargins(binding.tvMessage)
        measureChildWithMargins(
            binding.flexbox,
            widthMeasureSpec,
            usedWidth,
            heightMeasureSpec,
            usedHeight
        )
        usedWidth += getWidthWithMargins(binding.flexbox)
        usedHeight += getHeightWithMargins(binding.flexbox)

        setMeasuredDimension(
            resolveSize(usedWidth, widthMeasureSpec),
            resolveSize(usedHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        with(binding) {
            ivAvatar.layout(avatarRect, 0, 0)
            cvBackground.layout(backgroundRect, avatarRect.right, 0)
            tvUsername.layout(usernameRect, 0, 0)
            tvMessage.layout(messageRect, 0, usernameRect.bottom)
            flexbox.layout(flexboxRect, avatarRect.right, messageRect.bottom)
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)
}

private fun getWidthWithMargins(view: View): Int {
    val viewLP = view.layoutParams as ViewGroup.MarginLayoutParams
    return view.measuredWidth + viewLP.run { leftMargin + rightMargin }
}

private fun getHeightWithMargins(view: View): Int {
    val viewLP = view.layoutParams as ViewGroup.MarginLayoutParams
    return view.measuredHeight + viewLP.run { topMargin + bottomMargin }
}

private fun View.layout(rect: Rect) {
    with(rect) {
        layout(left, top, right, bottom)
    }
}

private fun View.layout(
    rect: Rect,
    leftBorder: Int,
    topBorder: Int
) {
    val viewLP = this.layoutParams as ViewGroup.MarginLayoutParams
    rect.left = leftBorder + viewLP.leftMargin
    rect.top = topBorder + viewLP.topMargin
    rect.right = rect.left + this.measuredWidth + viewLP.rightMargin
    rect.bottom = rect.top + this.measuredHeight + viewLP.bottomMargin
    this.layout(rect)
}

