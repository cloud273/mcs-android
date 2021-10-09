package com.cloud273.backend.util

import com.dungnguyen.qdcore.extension.toDate
import com.dungnguyen.qdcore.extension.toGMTDate
import com.dungnguyen.qdcore.extension.toGMTString
import com.dungnguyen.qdcore.extension.toString
import java.util.*

//******** API **********//

const val apiDateTimeFormat: String = "yyyy-MM-dd'T'HH:mm:ssZ"

fun Date.toApiDateTimeString(): String {
    val string = toGMTString(apiDateTimeFormat)
    val b = string.substring(startIndex = 0, endIndex = string.length - 2)
    val a = string.substring(startIndex = string.length - 2)
    return "$b:$a"
}

fun String.toDateApiDateTimeFormat(): Date? {
    return toGMTDate(apiDateTimeFormat)
}

const val apiDateFormat: String = "yyyy-MM-dd"

fun Date.toApiDateString(): String {
    return toString(apiDateFormat)
}

fun String.toDateApiDateFormat(): Date? {
    return toDate(apiDateFormat)
}

const val apiTimeFormat: String = "HH:mm:ss"

fun Date.toApiTimeString(): String {
    return toString(apiTimeFormat)
}