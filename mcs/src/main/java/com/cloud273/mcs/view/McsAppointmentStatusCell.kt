package com.cloud273.mcs.view

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.cloud273.backend.model.common.McsAptStatus
import com.cloud273.mcs.R
import com.cloud273.mcs.model.getString
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class McsAppointmentStatusCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.cell_mcs_appointment_status
    }

    interface OnActionInterface {
        fun onInfoClick(cell: McsAppointmentStatusCell, id: Any?)
    }

    class Model(val status: McsAptStatus, val isHiddenInfo: Boolean)

    var listener: OnActionInterface? = null

    private val infoBtn: Button = itemView.findViewById(R.id.infoBtn)
    private val statusTV: TextView = itemView.findViewById(R.id.statusTV)

    init {
        infoBtn.setOnClickListener {
            listener?.onInfoClick(this, id)
        }
    }

    override fun setup(obj: Any) {
        val model = obj as Model
        statusTV.text = model.status.value.getString()
        infoBtn.visibility = if (model.isHiddenInfo) View.GONE else View.VISIBLE
    }

}