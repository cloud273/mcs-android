package com.dungnguyen.qdcore.extension

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.dismissFocusIfNeed() {
    val v = activity!!.window.currentFocus;
    if (v != null) {
        v.clearFocus()
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }
}