package com.dungnguyen.qdcore.recycler.view

import android.view.ViewGroup
import android.widget.Button
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.model.TextInterface
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerButtonCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_cell_button
    }

    interface OnActionInterface {
        fun onButtonClick(cell: RecyclerButtonCell, id: Any?) = Unit
    }

    var listener: OnActionInterface? = null

    private val button: Button = itemView.findViewById(R.id.button)

    init {
        button.setOnClickListener {
            listener?.onButtonClick(this, id)
        }
    }

    override fun setup(obj: Any) {
        if (obj is String) {
            button.text = obj
        } else {
            val data = (obj as TextInterface)
            button.text = data.getText()
        }
    }

}



