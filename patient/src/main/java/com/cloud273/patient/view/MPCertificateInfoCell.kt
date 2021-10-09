package com.cloud273.patient.view

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.backend.model.doctor.McsDoctorCert
import com.cloud273.mcs.model.title

class MPCertificateInfoCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        const val cellId = R.layout.cell_mp_certificate_info
    }

    interface OnActionInterface {
        fun onInfoClick(cell: MPCertificateInfoCell, id: Any?, data: McsCertificate)
    }

    var listener: OnActionInterface? = null

    private val titleTV: TextView = itemView.findViewById(R.id.titleTV)
    private val detailTV: TextView = itemView.findViewById(R.id.detailTV)
    private val infoBtn: Button = itemView.findViewById(R.id.infoBtn)

    init {
        infoBtn.setOnClickListener {
            listener?.onInfoClick(this, id, data as McsCertificate)
        }
    }

    override fun setup(obj: Any) {
        val certificate = obj as McsCertificate
        titleTV.text = certificate.title
        detailTV.text = certificate.name
    }

}