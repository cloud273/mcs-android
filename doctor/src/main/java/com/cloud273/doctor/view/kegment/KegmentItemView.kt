package com.cloud273.doctor.view.kegment

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.min

class KegmentItemView: AppCompatTextView {
    constructor(context: Context): super(context, null) { initData() }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet, 0) { initData() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { initData() }

    private fun initData() {
        text = "Hello"
        gravity = Gravity.CENTER
        setTextColor(resources.getColor(android.R.color.black))
    }

    val defaultWidth = toPixel(80).toInt()
    val defaultHeight = toPixel(28).toInt()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (wMode) {
            MeasureSpec.EXACTLY -> w
            MeasureSpec.AT_MOST -> min(w, defaultWidth)
            else -> defaultWidth
        }
        val height = when (hMode) {
            MeasureSpec.EXACTLY -> h
            MeasureSpec.AT_MOST -> min(h, defaultHeight)
            else -> defaultHeight
        }
        setMeasuredDimension(width, height)
    }

    private fun toPixel(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    private fun toPixel(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}