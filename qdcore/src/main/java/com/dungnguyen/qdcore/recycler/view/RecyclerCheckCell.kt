package com.dungnguyen.qdcore.recycler.view

import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerCheckCell(viewGroup: ViewGroup, resource: Int): RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_cell_check
    }

    class Model(val title: String, val value: Boolean)

    interface OnActionInterface {
        fun onSwitchChange(cell: RecyclerCheckCell, id: Any?, value: Boolean) = Unit
    }

    var listener: OnActionInterface? = null

    private val textView: TextView = itemView.findViewById(R.id.textView)
    private val aSwitch: Switch = itemView.findViewById(R.id.aSwitch)

    init {
        aSwitch.setOnCheckedChangeListener { _, isChecked ->
            listener?.onSwitchChange(this, id, isChecked)
        }
    }

    override fun setup(obj: Any) {
        val data = obj as Model
        textView.text = data.title
        aSwitch.isChecked = data.value
    }

}