package com.dungnguyen.qdcore.recycler.common

open class RecyclerSectionData(val header: RecyclerCellData? = null, val cells: List<RecyclerCellData>, val footer: RecyclerCellData? = null) {

    fun isEmpty(): Boolean {
        return cells.isEmpty() && header == null && footer == null
    }

}