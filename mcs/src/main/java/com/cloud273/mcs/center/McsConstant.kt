package com.cloud273.mcs.center

import com.dungnguyen.qdcore.extension.Date

const val accountDidClearNotification = "accountDidClearNotification"
const val accountDidSetNotification = "accountDidSetNotification"
const val accountGenderDidUpdatedNotification = "accountGenderDidUpdatedNotification"
const val accountHealthInfoDidChangeNotification = "accountHealthInfoDidChangeNotification"
const val accountInfoDidChangeNotification = "accountInfoDidChangeNotification"
const val appointmentDidUpdatedNotification = "appointmentDidUpdatedNotification"
const val moveToAppointmentListNotification = "moveToAppointmentListNotification"
const val moveToBookingNotification = "moveToBookingNotification"
const val scheduleDidUpdatedNotification = "scheduleDidUpdatedNotification"


const val defaultCreatableEnd: Int = 3600*(-18)
const val defaultAcceptableEnd: Int = 3600*(-6)
const val defaultCancelableEnd: Int = 3600*(-6)
const val defaultRejectableEnd: Int = 3600*(-6)
const val defaultBeginableFrom: Int = 3600*(-12)
const val defaultBeginableEnd: Int = 3600*(12)
const val defaultFinishableEnd: Int = 3600*(12)

val minDob = Date(0, 0, 0, 1, 1, 1930)