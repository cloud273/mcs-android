package com.cloud273.doctor.model

import android.view.ViewGroup
import com.cloud273.doctor.view.*
import com.cloud273.mcs.model.McsRecyclerRender
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

class MDRecyclerRender() : McsRecyclerRender() {

    override fun additionRender(viewGroup: ViewGroup, resource: Int): RecyclerCell {
        val cell : RecyclerCell
        when (resource) {
            MDAppointmentCell.cellId -> cell = MDAppointmentCell(viewGroup, resource)
            MDCertificateInfoCell.cellId -> cell = MDCertificateInfoCell(viewGroup, resource)
            MDOptionCell.cellId -> cell = MDOptionCell(viewGroup, resource)
            MDDoctorCell.cellId -> cell = MDDoctorCell(viewGroup, resource)
            MDPackageCell.cellId -> cell = MDPackageCell(viewGroup, resource)
            else -> cell = super.additionRender(viewGroup, resource)
        }
        return cell
    }
}