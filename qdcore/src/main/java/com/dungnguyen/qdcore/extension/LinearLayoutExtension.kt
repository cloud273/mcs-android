package com.dungnguyen.qdcore.extension

import android.view.View
import android.widget.LinearLayout

fun LinearLayout.addView(view: View, left: Int, top: Int, right: Int, bottom: Int) {
    val d = context!!.density()
    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    params.setMargins((d * left).toInt(), (d * top).toInt(),(d * right).toInt(), (d * bottom).toInt())
    addView(view, params)
}