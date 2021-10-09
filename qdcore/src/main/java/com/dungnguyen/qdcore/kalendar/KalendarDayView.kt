package com.dungnguyen.qdcore.kalendar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.TypedValue
import android.view.Gravity
import android.view.animation.OvershootInterpolator
import androidx.appcompat.widget.AppCompatRadioButton
import com.dungnguyen.qdcore.extension.dayOfMonth
import com.dungnguyen.qdcore.extension.isSameDayAs
import java.util.*
import kotlin.math.min

class KalendarDayView(context: Context, private var listener: ListenerInterface): AppCompatRadioButton(context, null, 0) {

    interface ListenerInterface {

        fun todayColor(): Int

        fun selectedColor(): Int

        fun selectedBackgroundColor(): Int

        fun disableColor(): Int

        fun normalColor(): Int

        fun eventColor(): Int

        fun isInMonth(date: Date): Boolean

    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radiusBackGround = 0f
    private val defaultSize = toPixel(32).toInt()
    private lateinit var _date: Date

    val date: Date
        get() = _date

    private var numberOfEvents: Int = 0

    init {
        gravity = Gravity.CENTER
    }

    fun update(date: Date, isSelected: Boolean, isSelectable: Boolean, numberOfEvents: Int) {
        this._date = date
        this.isSelected = isSelected
        this.isEnabled = isSelectable
        this.numberOfEvents = numberOfEvents
        text = "${date.dayOfMonth()}"
    }

    fun update(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    fun reloadView(animation: Boolean) {
        animateBackground(animation)
        val color = if (isSelected) {
            listener.selectedColor()
        } else {
            if (isEnabled) {
                if (date.isSameDayAs(Date())) {
                    listener.todayColor()
                } else {
                    if (listener.isInMonth(date)) {
                        listener.normalColor()
                    } else {
                        listener.disableColor()
                    }
                }
            } else {
                listener.disableColor()
            }
        }
        setTextColor(color)
    }

    private fun animateBackground(animation: Boolean) {
        val size = if (width == 0 || height == 0) defaultSize else min(width, height)
        val animator: ValueAnimator = if (isSelected) {
            ValueAnimator.ofFloat(0f, size/2 - (toPixel(4f)))
        } else {
            ValueAnimator.ofFloat(size/2 - (toPixel(4f)), 0f)
        }
        animator.interpolator = OvershootInterpolator()
        if (animation) {
            animator.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        } else {
            animator.duration = 0
        }
        animator.addUpdateListener {
            radiusBackGround = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (wMode) {
            MeasureSpec.EXACTLY -> w
            MeasureSpec.AT_MOST -> min(w, defaultSize)
            else -> defaultSize
        }
        val height = when (hMode) {
            MeasureSpec.EXACTLY -> h
            MeasureSpec.AT_MOST -> min(h, defaultSize)
            else -> defaultSize
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas, paint)
        drawEvents(canvas, paint)
        super.onDraw(canvas)
    }

    private fun drawBackground(canvas: Canvas, paint: Paint) {
        paint.color = listener.selectedBackgroundColor()
        canvas.drawCircle(
            (width/2).toFloat(),
            (height/2).toFloat(),
            radiusBackGround,
            paint
        )

    }

    private fun drawEvents(canvas: Canvas, paint: Paint) {
        val numberOfEvents = numberOfEvents
        if (numberOfEvents > 0) {
            paint.color = listener.eventColor()
            val w = if (width == 0) defaultSize else min(defaultSize, width)
            val cy = height  - (toPixel(2))
            val cx = width/2f
            val radius: Float = min(toPixel(1.5f), (w/(numberOfEvents*2f)))
            val padding: Float = radius / 2f
            val distance: Float = radius * 2f + padding
            val e: Float = ((numberOfEvents - 1) / 2f)
            for (i in 0 until numberOfEvents) {
                val x: Float = (i.toFloat() - e) * distance + cx
                canvas.drawCircle(
                    x,
                    cy,
                    radius,
                    paint
                )
            }
        }
    }

    private fun toPixel(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)

    private fun toPixel(dp: Int) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics)
}