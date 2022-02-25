package com.setjy.practiceapp.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import com.setjy.practiceapp.util.dpToPx

class FlexboxLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    init {
//        setWillNotDraw(true)
    }

    private var childWidth: Int = 0
    private var childHeight: Int = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var currentWidth = 0
        var currentHeight = 0
        val parentWidth: Int = measuredWidth

        children.forEach { child ->

            val childLP = child.layoutParams as MarginLayoutParams

            measureChildWithMargins(
                child,
                widthMeasureSpec,
                currentWidth,
                heightMeasureSpec,
                currentHeight
            )
            childLP.apply {
                rightMargin = context.dpToPx(7F)
                bottomMargin = context.dpToPx(10F)
            }


            childWidth = child.measuredWidth + childLP.run { leftMargin + rightMargin }
            childHeight = child.measuredHeight + childLP.run { topMargin + bottomMargin }
            when {
                currentWidth + childWidth <= parentWidth -> currentWidth += childWidth

                currentWidth + childWidth > parentWidth -> {
                    currentHeight += childHeight
                    currentWidth = childWidth
                }
            }
        }
        currentHeight += childHeight
        setMeasuredDimension(
            resolveSize(parentWidth, widthMeasureSpec),
            resolveSize(currentHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var top = 0
        var left = 0
        val parentWidth: Int = measuredWidth
        children.forEach { child ->

            val childLP = child.layoutParams as MarginLayoutParams
            childWidth = child.measuredWidth + childLP.run { leftMargin + rightMargin }
            childHeight = child.measuredHeight + childLP.run { topMargin + bottomMargin }
            childLP.apply {
                rightMargin = context.dpToPx(7F)
                bottomMargin = context.dpToPx(10F)
            }
            when {
                left + childWidth <= parentWidth -> {
                    with(childLP) {
                        child.layout(
                            left + leftMargin,
                            top + topMargin,
                            left + childWidth - rightMargin,
                            top + childHeight - bottomMargin
                        )
                    }
                    left += childWidth
                }
                left + childWidth > parentWidth -> {
                    left = 0
                    top += childHeight
                    with(childLP) {
                        child.layout(
                            left + leftMargin,
                            top + topMargin,
                            left + childWidth - rightMargin,
                            top + childHeight - bottomMargin
                        )
                    }
                    left += childWidth
                }

            }
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams =
        MarginLayoutParams(context, attrs)

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams = MarginLayoutParams(p)
}
