package com.dungnguyen.qdcore.extension

import android.app.Activity
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Activity.resourceColor(value: Int): Int {
    return ContextCompat.getColor(this, value)
}

fun Fragment.resourceColor(value: Int): Int {
    return ContextCompat.getColor(requireContext(), value)
}

fun View.resourceColor(value: Int): Int {
    return ContextCompat.getColor(context!!, value)
}

fun View.resourceBackgroundColor(value: Int) {
    this.setBackgroundColor(resourceColor(value))
}

fun TextView.resourceTextColor(value: Int) {
    this.setTextColor(resourceColor(value))
}