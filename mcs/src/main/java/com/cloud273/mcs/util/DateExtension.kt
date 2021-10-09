package com.cloud273.mcs.util

import com.cloud273.mcs.center.instanceDatabase
import com.dungnguyen.qdcore.extension.toString
import java.util.*

const val appMonthYearFormat: String = "MM-yyyy"

fun Date.toAppMonthYearString() : String {
    return toString(appMonthYearFormat, local = instanceDatabase.language.value)
}

const val appDateTimeFormat: String = "dd-MM-yyyy hh:mm a"

fun Date.toAppDateTimeString() : String {
    return toString(appDateTimeFormat, local = instanceDatabase.language.value)
}

const val appDateFormat: String = "dd-MM-yyyy"

fun Date.toAppDateString() : String {
    return toString(appDateFormat, local = instanceDatabase.language.value)
}

const val appTimeFormat: String = "hh:mm a"

fun Date.toAppTimeString() : String {
    return toString(appTimeFormat, local = instanceDatabase.language.value)
}


