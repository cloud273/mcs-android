package com.dungnguyen.qdcore.model

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.dungnguyen.qdcore.extension.screenSize
import com.dungnguyen.qdcore.extension.toBitmap
import com.squareup.picasso.Picasso

class ImageModel {

    private var url: String? = null
    private var image: Bitmap? = null
    private var data: ByteArray? = null
    private var placeholder: Int? = null
    private var imageRatio: Float? = null
    private var screenWidthRatio: Float? = null
    private var screenHeightRatio: Float? = null

    constructor(url: String?, placeholder: Int?, imageRatio: Float? = null, screenWidthRatio: Float? = null, screenHeightRatio: Float? = null) {
        this.url = url
        this.placeholder = placeholder
        this.imageRatio = imageRatio
        this.screenWidthRatio = screenWidthRatio
        this.screenHeightRatio = screenHeightRatio
    }

    constructor(image: Bitmap?, placeholder: Int?, imageRatio: Float? = null, screenWidthRatio: Float? = null, screenHeightRatio: Float? = null) {
        this.image = image
        this.placeholder = placeholder
        this.imageRatio = imageRatio
        this.screenWidthRatio = screenWidthRatio
        this.screenHeightRatio = screenHeightRatio
    }

    constructor(data: ByteArray?, placeholder: Int?, imageRatio: Float? = null, screenWidthRatio: Float? = null, screenHeightRatio: Float? = null) {
        this.data = data
        this.placeholder = placeholder
        this.imageRatio = imageRatio
        this.screenWidthRatio = screenWidthRatio
        this.screenHeightRatio = screenHeightRatio
    }

    fun set(imageView: ImageView) {
        url?.also {
            if (placeholder == null) {
                Picasso.get().load(it).into(imageView)
            } else {
                Picasso.get().load(it).placeholder(placeholder!!).into(imageView)
            }
        }
        image?.also {
            imageView.setImageBitmap(it)
        }
        data?.also {
            imageView.setImageBitmap(it.toBitmap())
        }
        imageRatio?.also {
            (imageView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = it.toString()
        }
        screenWidthRatio?.also {
            imageView.layoutParams.width = (imageView.context!!.screenSize().width.toFloat()/2.5).toInt()
        }
        screenHeightRatio?.also {
            imageView.layoutParams.height = (imageView.context!!.screenSize().height.toFloat()/2.5).toInt()
        }
    }

}