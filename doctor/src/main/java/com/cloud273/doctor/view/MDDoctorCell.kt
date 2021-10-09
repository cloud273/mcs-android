package com.cloud273.doctor.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.backend.model.common.McsAccount
import com.cloud273.doctor.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.util.toAppDateString
import com.cloud273.localization.localized
import com.squareup.picasso.Picasso

class MDDoctorCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val genderTV: TextView = itemView.findViewById(R.id.genderTV)
    private val usernameTV: TextView = itemView.findViewById(R.id.usernameTV)
    private val dobTV: TextView = itemView.findViewById(R.id.dobTV)

    companion object {
        const val cellId = R.layout.cell_md_doctor
    }

    @SuppressLint("SetTextI18n")
    override fun setup(obj: Any) {
        super.setup(obj)
        val doctor = obj as McsAccount
        nameTV.text = doctor.profile!!.fullName
        usernameTV.text = doctor.username
        genderTV.text = doctor.profile!!.gender.getString()
        dobTV.text = "${"Dob".localized}: ${doctor.profile!!.dob.toAppDateString()}"
        Picasso.get().load(doctor.image).placeholder(R.mipmap.doctor_icon).into(imageView)
    }

}