package com.dungnguyen.qdcore.recycler.common

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.listLayout(divider: Boolean = true) {
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = RecyclerView.VERTICAL
    this.layoutManager = layoutManager
    if (divider) {
        addItemDecoration(RecyclerDividerItemDecoration(context))
    }
}

fun RecyclerView.unDrawDividerAt(vararg positions : Int) {
    (getItemDecorationAt(0) as? RecyclerDividerItemDecoration)?.also { decoration ->
        val list = mutableListOf<Int>()
        for (index in positions) {
            list.add(index)
        }
        decoration.unDrawPositions(list)
    }
}

fun RecyclerView.unDrawDividerAt(list: List<Int>) {
    (getItemDecorationAt(0) as? RecyclerDividerItemDecoration)?.also { decoration ->
        decoration.unDrawPositions(list)
    }
}

fun RecyclerView.drawAllDivider() {
    (getItemDecorationAt(0) as? RecyclerDividerItemDecoration)?.also { decoration ->
        decoration.unDrawPositions(null)
    }
}

fun RecyclerView.unDrawDivider() {
    if (itemDecorationCount > 0) {
        removeItemDecorationAt(0)
    }
}

fun RecyclerView.gridVerticalLayout(spanCount: Int, spacing: Int) {
    val layoutManager = GridLayoutManager(context, spanCount)
    layoutManager.orientation = RecyclerView.VERTICAL
    this.layoutManager = layoutManager
    addItemDecoration(OffsetItemDecoration(spanCount, spacing, true))
}
