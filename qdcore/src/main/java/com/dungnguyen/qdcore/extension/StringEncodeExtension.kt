package com.dungnguyen.qdcore.extension

import android.os.Build
import java.net.URLEncoder
import java.util.*

fun String.encodeQuery(): String {
    return URLEncoder.encode(this, "utf-8")
}

fun String.base64Encode(): String {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        return Base64.getEncoder().encodeToString(this.toByteArray())
    } else {
        return android.util.Base64.encodeToString(this.toByteArray(), 0)
    }


}