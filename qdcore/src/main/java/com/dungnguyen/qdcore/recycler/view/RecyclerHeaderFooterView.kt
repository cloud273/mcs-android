package com.dungnguyen.qdcore.recycler.view

import android.view.ViewGroup
import android.widget.TextView
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.model.TextInterface
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerHeaderFooterView (viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_header_footer_text
    }

    private val textView: TextView = itemView.findViewById(R.id.textView)

    override fun setup(obj: Any) {
        if (obj is String) {
            textView.text = obj
        } else {
            val data = (obj as TextInterface)
            textView.text = data.getText()
        }
    }

}