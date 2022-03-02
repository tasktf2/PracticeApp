package com.setjy.practiceapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.setjy.practiceapp.R
import com.setjy.practiceapp.util.dpToPx
import com.setjy.practiceapp.util.getEmojiByUnicode
import com.setjy.practiceapp.util.spToPx

class EmojiView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var emojiUnicode: String = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }
    var emojiCounter: Int = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    @ColorInt
    private var counterColor: Int = 0

    @ColorInt
    private var colorNotSelected: Int = 0

    @ColorInt
    private var colorSelected: Int = 0

    private var flagIsSelected: Boolean

    private var emojiWidth: Float = 0F
    private var emojiHeight: Float = 0F

    private var cornerRadius = context.dpToPx(10F).toFloat()
    private val textMargin = context.spToPx(6F)
    private val bgMargin = context.dpToPx(10F)

    private val emojiPoint = PointF()
    private val counterPoint = PointF()
    private val paddingPaint = Paint() //used for offset

    private val bgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val emojiPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
    }

    private var emojiSize: Float
        get() = emojiPaint.textSize
        set(value) {
            if (emojiPaint.textSize != value) {
                emojiPaint.textSize = value
                requestLayout()
            }
        }

    init {
        context.obtainStyledAttributes(attrs, R.styleable.EmojiView).apply {
            emojiUnicode = getEmojiByUnicode(
                getString(R.styleable.EmojiView_cl_emoji) ?: DEFAULT_EMOJI_UNICODE
            )
            emojiSize = getDimensionPixelSize(
                R.styleable.EmojiView_cl_text_size, context.spToPx(
                    DEFAULT_FONT_SIZE_PX
                )
            ).toFloat()
            emojiCounter = getInt(R.styleable.EmojiView_cl_counter, 0)
            counterColor = getColor(
                R.styleable.EmojiView_cl_text_color,
                ResourcesCompat.getColor(resources, R.color.emoji_counter_white, null)
            )
            colorSelected = getColor(
                R.styleable.EmojiView_cl_bg_selected_color,
                ResourcesCompat.getColor(resources, R.color.emoji_bg_selected, null)
            )
            colorNotSelected = getColor(
                R.styleable.EmojiView_cl_bg_not_selected_color,
                ResourcesCompat.getColor(resources, R.color.emoji_bg_not_selected, null)
            )
            flagIsSelected = getBoolean(R.styleable.EmojiView_cl_state_selected, false)
            recycle()
        }
    }

    private val counterPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.LEFT
        textSize = emojiSize
        color = counterColor
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val counterString: String = emojiCounter.toString()
        emojiWidth = if (emojiUnicode == "+") {
            val defEmoji = getEmojiByUnicode(DEFAULT_EMOJI_UNICODE)
            emojiPaint.measureText(defEmoji, 0, defEmoji.length)
        } else {
            emojiPaint.measureText(emojiUnicode, 0, emojiUnicode.length)
        }
        val textWidth =
            emojiWidth + counterPaint.measureText(counterString, 0, counterString.length)
        emojiHeight = emojiPaint.fontMetrics.run { bottom - ascent }
        val contentWidth = textWidth + (paddingStart + paddingEnd + bgMargin) * 2f + textMargin
        val contentHeight = emojiHeight + paddingTop + paddingBottom + bgMargin * 2f
        setMeasuredDimension(contentWidth.toInt(), contentHeight.toInt())
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (emojiUnicode == "+") {
            emojiPoint.set(width / 2f, height / 2f)
        } else {
            val padding = paddingPaint.measureText("0") * (emojiCounter.toString().length - 1)
            val baseline = height / 1.6f

            emojiPoint.set(
                width / 2f - textMargin - padding,
                baseline
            )
            counterPoint.set(
                width / 2f + textMargin - padding,
                baseline
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        val canvasCount = canvas.save()
        setViewBackground(canvas)
        drawViewContent(canvas)
        canvas.restoreToCount(canvasCount)
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isSelected) mergeDrawableStates(drawableState, DRAWABLE_STATE)
        return drawableState
    }

    override fun performClick(): Boolean {
        if (emojiUnicode != "+") {
            toggleSelected()
        } else {
            Toast.makeText(context, "Bottom navigation imitation", Toast.LENGTH_SHORT).show()
        }
        return super.performClick()
    }

    private fun drawViewContent(canvas: Canvas) {
        with(canvas) {
            if (emojiUnicode == "+") {
                emojiPaint.apply {
                    color = counterColor
                    strokeWidth = context.dpToPx(1F).toFloat()
                }
                drawLine(
                    (width - emojiWidth) / 2,
                    height / 2f,
                    (width + emojiWidth) / 2,
                    height / 2f,
                    emojiPaint
                )
                drawLine(
                    width / 2f,
                    (height + emojiHeight) / 2,
                    width / 2f,
                    (height - emojiHeight) / 2f,
                    emojiPaint
                )
            } else {
                drawText(emojiUnicode, emojiPoint.x, emojiPoint.y, emojiPaint)
                drawText(emojiCounter.toString(), counterPoint.x, counterPoint.y, counterPaint)
            }
        }
    }

    private fun toggleSelected() {
        flagIsSelected = !flagIsSelected
        isSelected = flagIsSelected
        if (isSelected) emojiCounter++
        else emojiCounter--
    }

    private fun setViewBackground(canvas: Canvas) {
        bgPaint.apply {
            color = if (isSelected) colorSelected
            else colorNotSelected
        }
        canvas.drawRoundRect(
            0F,
            0F,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            cornerRadius,
            cornerRadius,
            bgPaint
        )
    }

    companion object {
        private const val DEFAULT_FONT_SIZE_PX = 14F
        private const val DEFAULT_EMOJI_UNICODE = "0x1f600"
        private val DRAWABLE_STATE = IntArray(1) { android.R.attr.state_selected }
    }
}
