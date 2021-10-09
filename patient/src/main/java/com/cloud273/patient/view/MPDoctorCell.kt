package com.cloud273.patient.view

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.patient.R
import com.dungnguyen.qdcore.extension.yearOld
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.specialtiesString
import com.cloud273.localization.localized
import com.squareup.picasso.Picasso

class MPDoctorCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    data class Data (val specialty: McsSpecialty, val doctor: McsDoctor)

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val genderTV: TextView = itemView.findViewById(R.id.genderTV)
    private val descTV: TextView = itemView.findViewById(R.id.descTV)
    private val clinicTV: TextView = itemView.findViewById(R.id.clinicTV)
    private val specialtyTV: TextView = itemView.findViewById(R.id.specialtyTV)

    companion object {
        const val cellId = R.layout.cell_mp_doctor
    }

    override fun setup(obj: Any) {
        super.setup(obj)
        val data = obj as Data
        val doctor = data.doctor
        Picasso.get().load(doctor.image).placeholder(R.mipmap.doctor_icon).into(imageView)
        genderTV.text = (doctor.profile!!.gender.getString() + ", " + doctor.profile!!.dob.yearOld().toString() + "Year_old".localized)
        nameTV.text = (doctor.title.localized + " " + doctor.profile!!.fullName)
        descTV.text = doctor.office
        clinicTV.text = doctor.clinic!!.name
        specialtyTV.text = doctor.specialtiesString(data.specialty.code)
    }

}