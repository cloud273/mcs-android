package com.dungnguyen.qdcore.recycler.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.model.TextDetailInterface
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerTextDetailCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_cell_text_detail
    }

    private val titleTV: TextView = itemView.findViewById(R.id.titleTV)
    private val detailTV: TextView = itemView.findViewById(R.id.detailTV)

    override fun setup(obj: Any) {
        val data = obj as TextDetailInterface
        titleTV.text = data.getText()
        detailTV.text = data.getDetailText()
        if (detailTV.text.isNullOrEmpty()) {
            detailTV.visibility = View.GONE
        } else {
            detailTV.visibility = View.VISIBLE
        }
    }

}



