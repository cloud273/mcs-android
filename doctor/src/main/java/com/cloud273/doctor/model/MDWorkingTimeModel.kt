package com.cloud273.doctor.model

import com.cloud273.doctor.fragment.setting.MDWorkingTime
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.util.toAppTimeString
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.model.TextInterface

data class MDWorkingTimeModel(val data: MDWorkingTime): TextInterface {
    override fun getText(): String {
        var text = "${"From".localized} ${data.begin.toAppTimeString()} ${"to".localized} ${data.end.toAppTimeString()}"
        if (data.isMultiplePackage) {
            text += " ${data.pack.type.getString()}"
        }
        return text
    }
}