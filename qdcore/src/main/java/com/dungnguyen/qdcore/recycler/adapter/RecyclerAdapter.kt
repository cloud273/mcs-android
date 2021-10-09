package com.dungnguyen.qdcore.recycler.adapter

import com.dungnguyen.qdcore.recycler.common.*

open class RecyclerAdapter(render: RecyclerRender, listener: RecyclerCellInterface? = null) : RecyclerAbstractAdapter(render, listener) {

    init {
        setData(listOf())
    }

    fun setData(list: List<RecyclerCellData>) {
        updateData(list)
        notifyDataSetChanged()
    }

}