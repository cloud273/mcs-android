package com.cloud273.doctor.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.doctor.R
import com.cloud273.mcs.util.toAppDateTimeString
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.mcs.model.specialty
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.name
import com.cloud273.localization.localized
import com.squareup.picasso.Picasso

class MDAppointmentCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val patientTV: TextView = itemView.findViewById(R.id.patientTV)
    private val specialtyTV: TextView = itemView.findViewById(R.id.specialtyTV)
    private val timeTV: TextView = itemView.findViewById(R.id.timeTV)
    private val statusTV: TextView = itemView.findViewById(R.id.statusTV)

    companion object {
        const val cellId = R.layout.cell_md_appointment
    }

    @SuppressLint("SetTextI18n")
    override fun setup(obj: Any) {
        super.setup(obj)
        val appointment = obj as McsAppointment
        Picasso.get().load(appointment.patientInfo!!.image).placeholder(R.mipmap.profile_icon).into(imageView)
        patientTV.text = appointment.patientInfo!!.profile!!.fullName
        specialtyTV.text = appointment.specialty.name
        timeTV.text = appointment.begin.toAppDateTimeString()
        statusTV.text = "${"Order".localized} ${appointment.order!!} - ${appointment.status!!.value.getString()}"
    }

}