package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.backend.model.common.McsPrice
import com.google.gson.annotations.SerializedName

class McsPackage(@SerializedName("specialty") val specialtyCode: String,
                 val price: McsPrice,
                 val type: McsPackageType,
                 val visitTime: Int,
                 val note: String?,
                 val schedules: List<McsSchedule>?,
                 val workingDays: List<McsWorkingDay>?) : McsBase()