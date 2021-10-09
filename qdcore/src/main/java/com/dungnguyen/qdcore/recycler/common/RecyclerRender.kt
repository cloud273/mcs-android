package com.dungnguyen.qdcore.recycler.common

import android.view.ViewGroup
import com.dungnguyen.qdcore.recycler.view.*

open class RecyclerRender  {

    fun render(viewGroup: ViewGroup, resource: Int): RecyclerCell {
        return when (resource) {
            RecyclerHeaderFooterView.cellId -> RecyclerHeaderFooterView(viewGroup, resource)
            RecyclerTextCell.cellId -> RecyclerTextCell(viewGroup, resource)
            RecyclerTextCenterCell.cellId -> RecyclerTextCell(viewGroup, resource)
            RecyclerTextDetailCell.cellId -> RecyclerTextDetailCell(viewGroup, resource)
            RecyclerTextDetailRightCell.cellId -> RecyclerTextDetailRightCell(viewGroup, resource)
            RecyclerCheckCell.cellId -> RecyclerCheckCell(viewGroup, resource)
            RecyclerImageCell.cellId -> RecyclerImageCell(viewGroup, resource)
            RecyclerCircleImageCell.cellId -> RecyclerCircleImageCell(viewGroup, resource)
            RecyclerButtonCell.cellId -> RecyclerButtonCell(viewGroup, resource)
            else -> additionRender(viewGroup, resource)
        }
    }

    open fun additionRender(viewGroup: ViewGroup, resource: Int): RecyclerCell {
        return RecyclerCell(viewGroup, resource)
    }

}