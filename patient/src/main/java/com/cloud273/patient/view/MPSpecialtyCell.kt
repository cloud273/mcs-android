package com.cloud273.patient.view

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.mcs.model.name
import com.squareup.picasso.Picasso

class MPSpecialtyCell(viewGroup: ViewGroup, resource: Int) : RecyclerCell(viewGroup, resource) {

    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)

    companion object {
        const val cellId = R.layout.cell_mp_specialty
    }

    override fun setup(obj: Any) {
        super.setup(obj)
        val specialty = obj as McsSpecialty
        nameTV.text = specialty.name
        Picasso.get().load(specialty.image).placeholder(R.mipmap.no_image_icon).into(imageView)
    }

}