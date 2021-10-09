package com.dungnguyen.qdcore.extension

import android.content.Context
import android.util.DisplayMetrics
import android.util.Size
import android.util.TypedValue
import android.view.WindowManager

fun Context.density(): Float {
    return resources.displayMetrics.density
}

fun Context.screenSize() : Size {
    val wm : WindowManager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val dpHeight = outMetrics.heightPixels
    val dpWidth = outMetrics.widthPixels
    return Size(dpWidth.toInt(), dpHeight.toInt())
}

fun Context.dipToPixels(dipValue: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, resources.displayMetrics)