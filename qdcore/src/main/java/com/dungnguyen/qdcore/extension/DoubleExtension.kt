package com.dungnguyen.qdcore.extension

import java.text.NumberFormat
import java.util.*

fun Double.toCurrency(locale: Locale): String {
    val format = NumberFormat.getCurrencyInstance(locale)
    return format.format(this)
}