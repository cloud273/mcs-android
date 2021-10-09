package com.dungnguyen.qdcore.recycler.adapter

import com.dungnguyen.qdcore.recycler.common.*

open class RecyclerSectionAdapter(render: RecyclerRender, listener: RecyclerCellInterface? = null): RecyclerAbstractAdapter(render, listener) {

    init {
        setData(listOf())
    }

    private lateinit var sections: List<RecyclerSectionData>

    fun setData(sections: List<RecyclerSectionData>) {
        this.sections = sections
        val list = mutableListOf<RecyclerCellData>()
        for (section in sections) {
            section.header?.also {
                list.add(it)
            }
            val cells = section.cells
            for (cell in cells) {
                list.add(cell)
            }
            section.footer?.also {
                list.add(it)
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
            var found = section.header == data || section.footer == data
            if (!found) {
                val cells = section.cells
                for (cell in cells) {
                    row += 1
                    if (cell == data) {
                        found = true
                        break
                    }
                }
            }
            if (found) {
                break
            }
        }
        return RecyclerIndex(sec, row)
    }

}