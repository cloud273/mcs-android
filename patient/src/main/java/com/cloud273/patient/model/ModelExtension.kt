package com.cloud273.patient.model

import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.mcs.model.beginable
import com.cloud273.mcs.model.cancelable
import com.cloud273.mcs.model.finishable

val McsAppointment.isActive: Boolean
    get() = beginable!! || finishable!! || cancelable!!