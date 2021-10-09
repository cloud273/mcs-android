package com.cloud273.doctor.view.kegment

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import com.cloud273.doctor.R


class KegmentView: LinearLayout {

    constructor(context: Context): super(context, null) { initData() }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet, 0) { initData() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { initData() }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private lateinit var left: KegmentItemView
    private lateinit var right: KegmentItemView
    private var isLeft = true
        set(value) {
            animateSelectedBackground()
            if (value) {
                left.setTextColor(resources.getColor(android.R.color.white))
                right.setTextColor(resources.getColor(android.R.color.black))
            } else {
                left.setTextColor(resources.getColor(android.R.color.black))
                right.setTextColor(resources.getColor(android.R.color.white))
            }
            sideChangedListener.invoke(value)
            field = value
        }

    private var selectedBackgroundOffset = if (isLeft) 0f else (left.defaultWidth.toFloat())

    var leftText = "Left"
        set(value) {
            field = value
            left.text = value
        }
    var rightText = "Right"
        set(value) {
            field = value
            right.text = value
        }

    var sideChangedListener: (left: Boolean) -> Unit = {}
        set(value) {
            field = value
            field.invoke(isLeft)
        }

    private fun initData() {
        inflate(context, R.layout.layout_kegment, this)
        isSaveEnabled = true

        orientation = HORIZONTAL
        weightSum = 2f
        left = KegmentItemView(context)
        left.id = View.generateViewId()
        right = KegmentItemView(context)
        right.id = View.generateViewId()
        val lp = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT,
            1f
        )
        left.layoutParams = lp
        left.text = leftText
        left.setOnClickListener {
            if (!isLeft) {
                isLeft = true
            }
        }
        right.layoutParams = lp
        right.text = rightText
        right.setOnClickListener {
            if (isLeft) {
                isLeft = false
            }
        }
        addView(left)
        addView(right)

        background = resources.getDrawable(R.drawable.rect_round_gray)
        isLeft = true
    }

    override fun onDraw(canvas: Canvas) {
        drawSelectedBackground(canvas)
        super.onDraw(canvas)
    }

    private fun drawSelectedBackground(canvas: Canvas) {
        paint.color = Color.parseColor("#40000000")
        canvas.drawRoundRect(toPixel(1) + selectedBackgroundOffset, toPixel(1), left.width.toFloat() + selectedBackgroundOffset, left.height.toFloat(), toPixel(8), toPixel(8), paint)
    }

    private fun animateSelectedBackground() {
        val animator: ValueAnimator = if (isLeft) {
            ValueAnimator.ofFloat(0f, left.width.toFloat())
        } else {
            ValueAnimator.ofFloat(left.width.toFloat(), 0f)
        }
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
        animator.addUpdateListener {
            selectedBackgroundOffset = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    private fun toPixel(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    private fun toPixel(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}