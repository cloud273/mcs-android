package com.dungnguyen.qdcore.recycler.view

import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerImageCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_cell_image
    }

    interface OnActionInterface {
        fun onImageClick(cell: RecyclerImageCell, id: Any?) = Unit
    }

    var listener: OnActionInterface? = null

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)

    init {
        imageView.setOnClickListener {
            listener?.onImageClick(this, id)
        }
    }

    override fun setup(obj: Any) {
        (obj as ImageModel).set(imageView)
    }

}