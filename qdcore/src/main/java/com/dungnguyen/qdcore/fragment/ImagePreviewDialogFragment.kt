package com.dungnguyen.qdcore.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.extension.screenSize
import com.dungnguyen.qdcore.model.ImageModel
import com.github.chrisbanes.photoview.PhotoView


class ImagePreviewDialogFragment : DialogFragment() {

    private lateinit var image: ImageModel

    companion object {

        fun show(fragment: Fragment, image: ImageModel) {
            ImagePreviewDialogFragment().setImage(image).show(fragment)
        }

        fun show(activity: AppCompatActivity, image: ImageModel) {
            ImagePreviewDialogFragment().setImage(image).show(activity)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_preview_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = view.findViewById<PhotoView>(R.id.imageView)
        imageView.maximumScale = 3.toFloat()
        imageView.layoutParams.width = context!!.screenSize().width
        imageView.layoutParams.height = context!!.screenSize().height
        image.set(imageView)
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setWindowAnimations(R.style.present_anim)
    }

    private fun setImage(image: ImageModel): ImagePreviewDialogFragment {
        this.image = image
        return this
    }

    private fun show(fragment: Fragment) {
        this.show(fragment.fragmentManager!!, null)
    }

    private fun show(activity: AppCompatActivity) {
        this.show(activity.supportFragmentManager, null)
    }

}