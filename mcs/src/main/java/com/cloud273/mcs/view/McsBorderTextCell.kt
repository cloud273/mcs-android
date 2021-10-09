package com.cloud273.mcs.view

import android.view.ViewGroup
import android.widget.LinearLayout
import com.cloud273.mcs.R
import com.dungnguyen.qdcore.extension.resourceBackgroundColor
import com.dungnguyen.qdcore.extension.resourceTextColor
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell

open class McsBorderTextCell(viewGroup: ViewGroup, resource: Int) : RecyclerTextCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.cell_mcs_border_text
    }

    fun additionSetup(isSelected: Boolean) {
        val linearLayout = itemView.findViewById<LinearLayout>(R.id.layout)
        if (isSelected) {
            linearLayout.resourceBackgroundColor(R.color.blue_color)
            textView.resourceTextColor(R.color.white_color)
        } else {
            linearLayout.background = linearLayout.context.getDrawable(R.drawable.rectangle_border)
            textView.resourceTextColor(R.color.black_color)
        }
    }

}