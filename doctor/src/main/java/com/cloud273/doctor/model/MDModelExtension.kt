package com.cloud273.doctor.model

import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.mcs.model.beginable
import com.cloud273.mcs.model.finishable
import com.cloud273.mcs.model.rejectable
import com.dungnguyen.qdcore.extension.isSameDayAs
import java.util.*

val McsAppointment.isActive: Boolean
    get() = beginable!! || finishable!! || rejectable!!

val McsAppointment.isToday: Boolean
    get() = begin.isSameDayAs(Date())