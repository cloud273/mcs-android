package com.cloud273.doctor.model

import com.cloud273.backend.model.common.McsTimeRange
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.model.TextDetailInterface

data class MDTimeModel(val weekDay: String, val timeRanges: List<McsTimeRange>?): TextDetailInterface {

    override fun getDetailText(): String? {
        var text = ""
        if (timeRanges.isNullOrEmpty()) {
            text = "No_schedule_on_weekday".localized
        } else {
            for (item in timeRanges) {
                if (text.isNotEmpty()) {
                    text += "\n"
                }
                if (timeRanges.size > 1) {
                    text += "+ "
                }
                text += "${item.fromString} - ${item.toString}"
            }
        }
        return text
    }

    override fun getText(): String = weekDay
}