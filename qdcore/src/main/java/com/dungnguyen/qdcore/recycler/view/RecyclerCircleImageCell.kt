package com.dungnguyen.qdcore.recycler.view

import android.view.ViewGroup
import android.widget.ImageView
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.extension.screenSize
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.recycler.common.RecyclerCell

open class RecyclerCircleImageCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    companion object {
        val cellId = R.layout.recycler_cell_circle_image
    }

    interface OnActionInterface {
        fun onImageClick(cell: RecyclerCircleImageCell, id: Any?) = Unit
    }

    var listener: OnActionInterface? = null

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)

    init {
        imageView.layoutParams.width = (viewGroup.context!!.screenSize().width.toFloat()/2.5).toInt()
        imageView.setOnClickListener {
            listener?.onImageClick(this, id)
        }
    }

    override fun setup(obj: Any) {
        (obj as ImageModel).set(imageView)
    }

}