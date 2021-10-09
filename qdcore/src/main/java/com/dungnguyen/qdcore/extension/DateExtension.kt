package com.dungnguyen.qdcore.extension

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun String.toDate(format : String, timeZone: TimeZone? = null, local: String = "en_US_POSIX"): Date? {
    val locale = Locale(local)
    val formatter = SimpleDateFormat(format, locale)
    formatter.calendar = GregorianCalendar(locale)
    if (timeZone != null) {
        formatter.timeZone = timeZone
    }
    return formatter.parse(this)
}

fun Date.toString(format : String, timeZone: TimeZone? = null, local: String = "en_US_POSIX") : String {
    val locale = Locale(local)
    val formatter = SimpleDateFormat(format, locale)
    formatter.calendar = GregorianCalendar(locale)
    if (timeZone != null) {
        formatter.timeZone = timeZone
    }
    return formatter.format(this)
}

fun Date.timeIntervalSinceDate(date: Date): Long {
    val tDiff = abs(time - date.time)
    return TimeUnit.MILLISECONDS.toDays(tDiff)
}

fun Date.firstDayOfThisMonth(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.set(Calendar.DAY_OF_MONTH, 1)
    c.clear(Calendar.HOUR_OF_DAY)
    c.clear(Calendar.MINUTE)
    c.clear(Calendar.SECOND)
    c.clear(Calendar.MILLISECOND)
    return c.time
}

fun Date.lastDayOfThisMonth(): Date = add(month = 1).firstDayOfThisMonth().add(second = -1)

fun String.toGMTDate(format : String, local: String = "en_US_POSIX"): Date? {
    return toDate(format, TimeZone.getTimeZone("UTC"), local)
}

fun Date.toGMTString(format : String, local: String = "en_US_POSIX") : String {
    return toString(format, TimeZone.getTimeZone("UTC"), local)
}

fun Date.calendar(local: String = "en_US_POSIX") : Calendar {
    val calendar = GregorianCalendar(Locale(local))
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.time = this
    return calendar
}

fun Date.add(second: Int = 0, minute: Int = 0, hour: Int = 0, day: Int = 0, month: Int = 0, year: Int = 0) : Date {
    val calendar = calendar()
    calendar.add(Calendar.SECOND, second)
    calendar.add(Calendar.MINUTE, minute)
    calendar.add(Calendar.HOUR, hour)
    calendar.add(Calendar.DAY_OF_MONTH, day)
    calendar.add(Calendar.MONTH, month)
    calendar.add(Calendar.YEAR, year)
    return calendar.time
}

fun Date.beginDate() : Date {
    val calendar = calendar()
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    return calendar.time
}

fun Date.endDate() : Date {
    val calendar = calendar()
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    return calendar.time
}

fun Date.year(): Int {
    return calendar().get(Calendar.YEAR)
}

fun Date.month(): Int {
    return calendar().get(Calendar.MONTH)
}

fun Date.dayOfMonth(): Int {
    return calendar().get(Calendar.DAY_OF_MONTH)
}

fun Date.dayOfWeek(): Int {
    return calendar().get(Calendar.DAY_OF_WEEK)
}

fun Date.yearOld(): Int {
    return Date().year() - this.year()
}

fun Date.isSameDayAs(date: Date): Boolean {
    val cal1 = date.calendar()
    val cal = calendar()
    return cal.get(Calendar.DAY_OF_YEAR) == cal1.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)
}

fun Date.isSameMonthAs(date: Date): Boolean {
    val cal1 = date.calendar()
    val cal = calendar()
    return cal.get(Calendar.MONTH) == cal1.get(Calendar.MONTH) && cal.get(Calendar.YEAR) == cal1.get(Calendar.YEAR)
}

fun Date(second: Int = 0, minute: Int = 0, hour: Int = 0, day: Int, month: Int, year: Int) : Date {
    val calendar = GregorianCalendar(Locale("en_US_POSIX"))
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.SECOND, second)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)
    return calendar.time
}
