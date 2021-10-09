package com.dungnguyen.qdcore.recycler.adapter

import com.dungnguyen.qdcore.recycler.common.*

open class RecyclerGridSectionAdapter(render: RecyclerRender, listener: RecyclerCellInterface? = null): RecyclerAbstractAdapter(render, listener) {

    init {
        setData(listOf())
    }

    private lateinit var sections: List<List<RecyclerCellData>>

    fun setData(sections: List<List<RecyclerCellData>>) {
        this.sections = sections
        val list = mutableListOf<RecyclerCellData>()
        for (section in sections) {
            for (cell in section) {
                list.add(cell)
            }
        }
        updateData(list)
        notifyDataSetChanged()
    }

    override fun indexOf(data: RecyclerCellData): RecyclerIndex {
        var sec = -1
        var row = -1
        for (section in sections) {
            sec += 1
            row = -1
            var found = false
            for (cell in section) {
                row += 1
                if (cell == data) {
                    found = true
                    break
                }
            }
            if (found) {
                break
            }
        }
        return RecyclerIndex(sec, row)
    }

}