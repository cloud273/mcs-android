package com.dungnguyen.qdcore.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun ByteArray.toBitmap(): Bitmap {
    // TODO: need test and improvoment
    return BitmapFactory.decodeByteArray(this, 0, size)
}