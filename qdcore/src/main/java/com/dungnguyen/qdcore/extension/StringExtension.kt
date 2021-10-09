package com.dungnguyen.qdcore.extension

import java.util.regex.Pattern

fun String.isEmail(): Boolean {
    return Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}").matcher(this).matches()
}

fun String.isVnMobile(): Boolean {
    return Pattern.compile("^\\+84\\d{9}$").matcher(this).matches()
}

fun String.isVnPhone(): Boolean {
    return Pattern.compile("^\\+84(\\d{4,6}|\\d{8,10})$").matcher(this).matches()
}

fun String.reformatVnPhone(): String? {
    if (this.count() >= 4) {
        val f = this.substring(0,1)
        if (f == "0") {
            return "+84" + this.substring(1)
        } else {
            val sub = this.substring(0,3)
            if (sub == "+84") {
                return this
            } else {
                return "+84" + this
            }
        }
    }
    return null
}

fun String.getVnPhone(): String? {
    val phone = this.reformatVnPhone()
    if (phone != null && phone.isVnPhone()) {
        return phone
    }
    return null
}

fun String.getVnMobile(): String? {
    val phone = this.reformatVnPhone()
    if (phone != null && phone.isVnMobile()) {
        return phone
    }
    return null
}

fun String.getVnMobileOrEmail(): String? {
    if (this.isEmail()) {
        return this
    } else {
        return this.getVnMobile()
    }
}