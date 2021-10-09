package com.cloud273.patient.view

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.mcs.util.toAppDateTimeString
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.name
import com.cloud273.mcs.model.specialty
import com.cloud273.localization.localized
import com.squareup.picasso.Picasso

class MPAppointmentCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val doctorTV: TextView = itemView.findViewById(R.id.doctorTV)
    private val specialtyTV: TextView = itemView.findViewById(R.id.specialtyTV)
    private val timeTV: TextView = itemView.findViewById(R.id.timeTV)
    private val statusTV: TextView = itemView.findViewById(R.id.statusTV)

    companion object {
        const val cellId = R.layout.cell_mp_appointment
    }

    override fun setup(obj: Any) {
        super.setup(obj)
        val appointment = obj as McsAppointment
        Picasso.get().load(appointment.doctorInfo!!.image).placeholder(R.mipmap.doctor_icon).into(imageView)
        doctorTV.text = (appointment.doctorInfo!!.title.localized + ":" +  appointment.doctorInfo!!.profile!!.fullName)
        specialtyTV.text = appointment.specialty.name
        timeTV.text = appointment.begin.toAppDateTimeString()
        statusTV.text = appointment.status!!.value.getString()
    }

}