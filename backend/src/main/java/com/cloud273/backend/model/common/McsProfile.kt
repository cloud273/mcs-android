package com.cloud273.backend.model.common

import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import java.util.*

class McsProfile(var firstname: String,
                 var lastname: String,
                 var gender: McsGender,
                 @JsonAdapter(DayTypeAdapter::class) var dob: Date) {

    companion object {

        fun create(firstname: String, lastname: String, gender: McsGender, dob: Date): McsProfile {
            return McsProfile(firstname, lastname, gender, dob)
        }

        fun update(firstname: String, lastname: String, gender: McsGender, dob: Date): McsProfile {
            return McsProfile(firstname, lastname, gender, dob)
        }

    }

}