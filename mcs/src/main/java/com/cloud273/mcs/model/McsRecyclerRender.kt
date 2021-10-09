package com.cloud273.mcs.model

import android.view.ViewGroup
import com.cloud273.mcs.util.*
import com.cloud273.mcs.view.McsAppointmentStatusCell
import com.cloud273.mcs.view.McsBorderTextCell
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.dungnguyen.qdcore.recycler.common.RecyclerRender
import com.dungnguyen.qdcore.recycler.view.RecyclerButtonCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell

open class McsRecyclerRender() : RecyclerRender() {

    override fun additionRender(viewGroup: ViewGroup, resource: Int): RecyclerCell {
        return when (resource) {
            RecyclerTextDetailCell.titleContentCell -> RecyclerTextDetailCell(viewGroup, resource)
            RecyclerTextDetailCell.titleContentHighlightCell -> RecyclerTextDetailCell(viewGroup, resource)
            RecyclerTextDetailCell.titleHighlightContentCell -> RecyclerTextDetailCell(viewGroup, resource)
            RecyclerButtonCell.redButtonCell -> RecyclerButtonCell(viewGroup, resource)
            RecyclerButtonCell.greenButtonCell -> RecyclerButtonCell(viewGroup, resource)
            RecyclerButtonCell.grayButtonCell -> RecyclerButtonCell(viewGroup, resource)
            RecyclerTextCell.blueTextCell -> RecyclerTextCell(viewGroup, resource)
            RecyclerTextCell.refreshCell -> RecyclerTextCell(viewGroup, resource)
            RecyclerTextCell.addTextCell -> RecyclerTextCell(viewGroup, resource)
            McsBorderTextCell.cellId -> McsBorderTextCell(viewGroup, resource)
            McsAppointmentStatusCell.cellId -> McsAppointmentStatusCell(viewGroup, resource)
            else -> super.additionRender(viewGroup, resource)
        }
    }

}