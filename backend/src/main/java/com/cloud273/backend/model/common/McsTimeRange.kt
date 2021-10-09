package com.cloud273.backend.model.common

import com.cloud273.backend.util.toApiTimeString
import com.google.gson.annotations.SerializedName
import java.util.*

class McsTimeRange(@SerializedName("from") var fromString: String,
                   @SerializedName("to") var toString: String) {

    companion object {

        fun create(from: Date, to: Date): McsTimeRange {
            return McsTimeRange(from.toApiTimeString(), to.toApiTimeString())
        }

        fun update(from: Date, to: Date): McsTimeRange {
            return McsTimeRange(from.toApiTimeString(), to.toApiTimeString())
        }

    }
}