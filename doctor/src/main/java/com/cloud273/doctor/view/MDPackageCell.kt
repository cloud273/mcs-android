package com.cloud273.doctor.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.doctor.R
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.mcs.model.durationString
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.name
import com.cloud273.mcs.model.specialty

class MDPackageCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.cell_md_package
    }

    private val titleTV: TextView
    private val priceTV: TextView
    private val durationTV: TextView
    private val specialtyTV: TextView

    init {
        titleTV = itemView.findViewById(R.id.titleTV)
        priceTV = itemView.findViewById(R.id.priceTV)
        durationTV = itemView.findViewById(R.id.durationTV)
        specialtyTV = itemView.findViewById(R.id.specialtyTV)
    }

    @SuppressLint("SetTextI18n")
    override fun setup(obj: Any) {
        super.setup(obj)
        val p = obj as McsPackage
        titleTV.text = p.type.getString()
        priceTV.text = "${"Price".localized}: ${p.price.getString()}"
        durationTV.text = "${"Appointment_duration".localized}: ${p.durationString}"
        specialtyTV.text = "${"Specialty".localized}: ${p.specialty.name}"
    }

}