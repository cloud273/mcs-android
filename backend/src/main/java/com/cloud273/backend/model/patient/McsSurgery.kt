package com.cloud273.backend.model.patient


import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

class McsSurgery(var name: String,
                 @JsonAdapter(DayTypeAdapter::class) var date: Date,
                 @SerializedName("description") var note: String?) : McsBase() {

    companion object {

        fun create(name : String, date: Date, note : String?): McsSurgery {
            return McsSurgery(name, date, note)
        }

        fun update(id: Int, name : String, date: Date, note : String?): McsSurgery {
            val result = McsSurgery(name, date, note)
            result.id = id
            return result
        }

    }

}