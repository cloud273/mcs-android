package com.cloud273.patient.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.TextView
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.doctor.McsClinic
import com.cloud273.mcs.model.getString
import com.cloud273.localization.localized

class MPAddressClinicCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        const val cellId = R.layout.cell_mp_address_clinic
        const val titleHighlightCellId = R.layout.cell_mp_address_title_highlight_clinic
        const val lineHighlightCellId = R.layout.cell_mp_address_line_highlight_clinic
    }

    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val phoneTV: TextView = itemView.findViewById(R.id.phoneTV)
    private val addressTV: TextView = itemView.findViewById(R.id.addressTV)

    @SuppressLint("SetTextI18n")
    override fun setup(obj: Any) {
        super.setup(obj)
        val clinic = obj as McsClinic
        nameTV.text = clinic.name
        phoneTV.text = "${"Phone".localized}: ${clinic.phone}"
        addressTV.text = "${"Address".localized}: ${clinic.address.getString()}"
    }

}