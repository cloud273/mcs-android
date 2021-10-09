package com.cloud273.patient.model

import android.view.ViewGroup
import com.cloud273.mcs.model.McsRecyclerRender
import com.cloud273.patient.view.*
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

class McsRecyclerRender() : McsRecyclerRender() {

    override fun additionRender(viewGroup: ViewGroup, resource: Int): RecyclerCell {
        return when (resource) {
            MPPatientCell.cellId -> MPPatientCell(viewGroup, resource)
            MPAppointmentCell.cellId -> MPAppointmentCell(viewGroup, resource)
            MPSpecialtyCell.cellId -> MPSpecialtyCell(viewGroup, resource)
            MPDoctorCell.cellId -> MPDoctorCell(viewGroup, resource)
            MPAddressClinicCell.cellId -> MPAddressClinicCell(viewGroup, resource)
            MPAddressClinicCell.titleHighlightCellId -> MPAddressClinicCell(viewGroup, resource)
            MPAddressClinicCell.lineHighlightCellId -> MPAddressClinicCell(viewGroup, resource)
            MPDoctorInfoCell.cellId -> MPDoctorInfoCell(viewGroup, resource)
            MPCertificateInfoCell.cellId -> MPCertificateInfoCell(viewGroup, resource)
            else -> super.additionRender(viewGroup, resource)
        }
    }

}