package com.dungnguyen.qdcore.extension

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import kotlin.math.min


fun Bitmap.square(): Bitmap {
    // TODO: chua kiem tra
    val size = min(width, height)
    val x = if (width > size) (width - size)/2 else 0
    val y = if (height > size) (height - size)/2 else 0
    return Bitmap.createBitmap(this, x, y, size, size)
}

fun Bitmap.scale(widthPix: Int, heightPix: Int): Bitmap = Bitmap.createScaledBitmap(this, widthPix, heightPix, true)

fun Bitmap.rotate(degree: Int): Bitmap {
    // TODO: chua kiem tra
    val mtx = Matrix()
    mtx.postRotate(degree.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width,height, mtx, true)
}

fun Bitmap.toByteArray(): ByteArray {
    // TODO: chua test ma hinh nhu no tao ra hinh co kich thuoc file to bat thuong
    //  em co the search cho su dung cai nay de xem => a dang nghi ngo ham nay co van de
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}