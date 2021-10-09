package com.cloud273.doctor.view

import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.doctor.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.mcs.model.title

class MDCertificateInfoCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        const val cellId = R.layout.cell_md_certificate_info
    }

    var listener: ((cell: MDCertificateInfoCell, id: Any?, data: McsCertificate) -> Unit)? = null

    private val titleTV: TextView = itemView.findViewById(R.id.titleTV)
    private val detailTV: TextView = itemView.findViewById(R.id.detailTV)
    private val infoBtn: Button = itemView.findViewById(R.id.infoBtn)

    init {
        infoBtn.setOnClickListener {
            listener?.invoke(this, id, data as McsCertificate)
        }
    }

    override fun setup(obj: Any) {
        super.setup(obj)
        val certificate = obj as McsCertificate
        titleTV.text = certificate.title
        detailTV.text = certificate.name
    }

}