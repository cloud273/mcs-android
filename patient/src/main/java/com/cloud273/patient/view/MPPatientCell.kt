package com.cloud273.patient.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.mcs.util.toAppDateString
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.localization.localized
import com.squareup.picasso.Picasso

class MPPatientCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {


    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val genderTV: TextView = itemView.findViewById(R.id.genderTV)
    private val usernameTV: TextView = itemView.findViewById(R.id.usernameTV)
    private val dobTV: TextView = itemView.findViewById(R.id.dobTV)

    companion object {
        const val cellId = R.layout.cell_mp_patient
    }

    @SuppressLint("SetTextI18n")
    override fun setup(obj: Any) {
        super.setup(obj)
        val patient = obj as McsPatient
        nameTV.text = patient.profile!!.fullName
        usernameTV.text = patient.username!!
        genderTV.text = patient.profile!!.gender.getString()
        dobTV.text = "${"Dob".localized}: ${patient.profile!!.dob.toAppDateString()}"
        Picasso.get().load(patient.image).placeholder(R.mipmap.profile_icon).into(imageView)
    }

}