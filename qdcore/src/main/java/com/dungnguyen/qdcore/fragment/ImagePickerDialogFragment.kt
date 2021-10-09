package com.dungnguyen.qdcore.fragment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dungnguyen.qdcore.extension.square
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

/*
    Add to AndroidManifest.xml

    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
*/

class ImagePickerDialogFragment : ActionSheetDialogFragment() {

    companion object {

        fun show(fragment: Fragment, title: String = "Add Photo", camera: String? = "Camera", gallery: String? = "Gallery", cancel: String = "Cancel", square: Boolean = false, completion: (Boolean, Bitmap?) -> Unit) {
            show(
                null,
                fragment,
                title,
                camera,
                gallery,
                cancel,
                square,
                completion
            )
        }

        fun show(activity: AppCompatActivity, title: String = "Add Photo", camera: String? = "Camera", gallery: String? = "Gallery", cancel: String = "Cancel", square: Boolean = false, completion: (Boolean, Bitmap?) -> Unit) {
            show(
                activity,
                null,
                title,
                camera,
                gallery,
                cancel,
                square,
                completion
            )
        }

        private fun show(activity: AppCompatActivity?, fragment: Fragment?, title: String, camera: String?, gallery: String?, cancel: String, square: Boolean, completion: (Boolean, Bitmap?) -> Unit) {
            val picker = ImagePickerDialogFragment()
                .setSquare(square)
                .setTitle(title)
                .setDestructive(cancel)
                .dismissOnClick(false) as ImagePickerDialogFragment
            if (activity != null) {
                picker.with(activity)
            }
            if (fragment != null) {
                picker.with(fragment)
            }
            if (camera != null){
                picker.showCamera(camera, completion)
            }
            if (gallery != null) {
                picker.showGallery(gallery, completion)
            }
            picker.show()
        }

    }

    private val REQUEST_CAMERA_CODE = 1
    private val REQUEST_GALLERY_CODE = 2

    private var fragment: Fragment? = null
    private var appCompatActivity: AppCompatActivity? = null

    private var square: Boolean = false

    private var cameraCompletion: ((Boolean, Bitmap?) -> Unit)? = null
    private var galleryCompletion: ((Boolean, Bitmap?) -> Unit)? = null

    fun with(fragment: Fragment): ImagePickerDialogFragment {
        this.fragment = fragment
        return this
    }

    fun with(activity: AppCompatActivity): ImagePickerDialogFragment {
        this.appCompatActivity = activity
        return this
    }

    fun setSquare(value: Boolean): ImagePickerDialogFragment {
        this.square = value
        return this
    }

//    TODO: Hinh lay ve tu method nay co the la hinh full or la hinh dc crop thanh hinh vuong.
//      Sau khi su dung method nay va chuyen thanh byte array de upload len server thi thay dung luong file lon qua.
//      Ko bit do cach lay hinh co van de hay ham fun Bitmap.toByteArray(): ByteArray co van de. E co the search 2 ham nay trong project de xem

    fun showCamera(camera: String = "Camera", completion: (Boolean, Bitmap?) -> Unit) {
        add(camera) {
            val activity = activity()
            Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                                takePictureIntent.resolveActivity(activity.packageManager)?.also {
                                    cameraCompletion = completion
                                    startActivityForResult(takePictureIntent, REQUEST_CAMERA_CODE)
                                }
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token!!.continuePermissionRequest()
                    }
                })
                .check()
        }
    }

    fun showGallery(gallery: String = "Gallery", completion: (Boolean, Bitmap?) -> Unit) {
        add(gallery) {
            val activity = activity()
            Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (report!!.areAllPermissionsGranted()) {
                            // TODO: Show gallary and select image
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token!!.continuePermissionRequest()
                    }
                })
                .check()
        }
    }

    fun show() {
        if (appCompatActivity != null) {
            show(appCompatActivity!!)
        } else {
            show(fragment!!)
        }
    }

    private fun activity(): Activity {
        val result: Activity
        if (appCompatActivity != null) {
            result = appCompatActivity!!
        } else {
            result = fragment!!.activity!!
        }
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        dismiss()
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_CODE) {
                val image = data!!.extras!!.get("data") as Bitmap
                val result: Bitmap
                if (square) {
                    result = image.square()
                } else {
                    result = image
                }
                cameraCompletion?.invoke(true, result)
            }
        }
    }

}