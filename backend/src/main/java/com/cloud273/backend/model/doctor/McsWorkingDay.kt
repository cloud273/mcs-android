package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.model.common.McsTimeRange
import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.util.*

class McsWorkingDay(@JsonAdapter(DayTypeAdapter::class) var date: Date,
                    var times: List<McsTimeRange>,
                    var packageId: Int) : McsBase() {

    companion object {
        fun create(date: Date, times: List<McsTimeRange>, packageId: Int): McsWorkingDay {
            return McsWorkingDay(date, times, packageId)
        }

        fun update(id: Int, date: Date, times: List<McsTimeRange>, packageId: Int): McsWorkingDay {
            val result = McsWorkingDay(date, times, packageId)
            result.id = id
            return result
        }
    }

}