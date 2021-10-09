package com.dungnguyen.qdcore.recycler.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class RecyclerCell (viewGroup: ViewGroup, resource: Int) : RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.context).inflate(resource, viewGroup, false)) {

    var id : Any? = null

    lateinit var data: Any

    open fun setup(obj: Any) {

    }

}