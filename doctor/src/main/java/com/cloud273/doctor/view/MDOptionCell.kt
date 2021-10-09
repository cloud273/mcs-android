package com.cloud273.doctor.view

import android.view.ViewGroup
import android.widget.Button
import com.cloud273.doctor.R
import com.cloud273.doctor.model.MDOptionModel
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

class MDOptionCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {
    companion object {
        val cellId = R.layout.cell_md_option
    }

    private val option1: Button = itemView.findViewById(R.id.negative)
    private val option2: Button = itemView.findViewById(R.id.positive)
    var option1Listener = {}
    var option2Listener = {}
    var option1Disabled: Boolean
        set(value) {
            option1.isEnabled = !value
        }
        get() = option1.isEnabled

    var option2Disabled: Boolean
        set(value) {
            option2.isEnabled = !value
        }
        get() = option2.isEnabled

    init {
        option1.setOnClickListener {
            option1Listener.invoke()
        }
        option2.setOnClickListener {
            option2Listener.invoke()
        }
    }

    override fun setup(obj: Any) {
        super.setup(obj)
        val data = obj as MDOptionModel
        option1.text = data.negativeText
        option2.text = data.positiveText
        data.nColor?.also {
            option1.setBackgroundColor(it)
        }
        data.pColor?.also {
            option2.setBackgroundColor(it)
        }
        data.nTextColor?.also {
            option1.setTextColor(it)
        }
        data.pTextColor?.also {
            option2.setTextColor(it)
        }
    }
}