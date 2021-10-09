package com.dungnguyen.qdcore.extension

import android.os.Build
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private fun getGSon(dateFormat: String? = null) : Gson {
    val builder = GsonBuilder()
    dateFormat?.also {
        builder.setDateFormat(dateFormat)
    }
    return builder.create()
}

fun Any.toJsonString(dateFormat: String? = null) : String? {
    return getGSon(dateFormat).toJson(this, this::class.java)
}

fun <T> String.toObject(type: Type, dateFormat: String? = null) : T? {
    return getGSon(dateFormat).fromJson(this, type)
}

fun <T> String.toArray(type: Type, dateFormat: String? = null) : List<T>? {
    val listType = TypeToken.getParameterized(List::class.java, type).type
    return getGSon(dateFormat).fromJson(this, listType)
}

val deviceInformation : String
    get() = Build.MANUFACTURER + " - " + Build.BRAND + " - " + Build.MODEL + " - " + Build.VERSION.SDK_INT