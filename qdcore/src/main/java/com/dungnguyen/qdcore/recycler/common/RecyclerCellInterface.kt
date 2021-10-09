package com.dungnguyen.qdcore.recycler.common

interface RecyclerCellInterface {

    fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) = Unit

    fun onSelect(cell: RecyclerCell, id: Any?, data: Any) = Unit

    fun onSelect(cell: RecyclerCell, index: RecyclerIndex) = Unit

    fun onLongPress(cell: RecyclerCell, id: Any?, data: Any) = Unit

    fun onLongPress(cell: RecyclerCell, index: RecyclerIndex) = Unit

}