package com.cloud273.patient.view

import android.view.ViewGroup
import android.widget.TextView
import com.cloud273.patient.R
import com.dungnguyen.qdcore.extension.yearOld
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.localization.localized

class MPDoctorInfoCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        const val cellId = R.layout.cell_mp_doctor_info
    }

    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val genderAgeTV: TextView = itemView.findViewById(R.id.genderAgeTV)

    override fun setup(obj: Any) {
        super.setup(obj)
        val doctor = obj as McsDoctor
        nameTV.text = (doctor.title + " " + doctor.profile!!.fullName)
        genderAgeTV.text = ("${doctor.profile!!.gender.getString()} / ${doctor.profile!!.dob.yearOld()}${"Year_old".localized}")
    }

}