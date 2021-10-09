package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.util.DateTimeTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.util.*

open class McsWorkingTime(@JsonAdapter(DateTimeTypeAdapter::class) val begin: Date,
                     @JsonAdapter(DateTimeTypeAdapter::class) val end: Date,
                     val visitTime: Int,
                     val packageId: Int?,
                     val scheduleId: Int?,
                     val workingDayId: Int?) : McsBase()