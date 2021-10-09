package com.dungnguyen.qdcore.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dungnguyen.qdcore.recycler.common.*

abstract class RecyclerAbstractAdapter(private val render: RecyclerRender, private val listener: RecyclerCellInterface? = null) : RecyclerView.Adapter<RecyclerCell>() {

    private lateinit var list: List<RecyclerCellData>

    protected open fun indexOf(data: RecyclerCellData): RecyclerIndex {
        return RecyclerIndex(0, list.indexOf(data))
    }

    protected fun updateData(list: List<RecyclerCellData>) {
        this.list = list
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerCell, position: Int) {
        val obj = list[position]
        holder.setup(obj.data)
        holder.id = obj.id
        holder.data = obj.data
        listener?.onDrawCell(holder, obj.id, obj.data)
        holder.itemView.setOnClickListener {
            val index = indexOf(obj)
            listener?.onSelect(holder, obj.id, obj.data)
            listener?.onSelect(holder, index)
        }
        holder.itemView.setOnLongClickListener {
            val index = indexOf(obj)
            listener?.onLongPress(holder, obj.id, obj.data)
            listener?.onLongPress(holder, index)
            return@setOnLongClickListener true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].resource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerCell {
        return render.render(parent, viewType)
    }

}