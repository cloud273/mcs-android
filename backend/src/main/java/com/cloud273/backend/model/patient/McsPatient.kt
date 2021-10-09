package com.cloud273.backend.model.patient

import com.cloud273.backend.model.common.*
import com.google.gson.annotations.SerializedName
import java.util.*

class McsPatient(username: String?,
                 imageName: String?,
                 language: McsLanguage?,
                 profile: McsProfile?,
                 var address: McsAddress?) : McsAccount(username, imageName, language, profile) {

    var allergies: List<McsAllergy>? = null
    var surgeries: List<McsSurgery>? = null
    @SerializedName("medications") var _medications: List<McsMedication>? = null

    companion object {
        fun create(username: String, image: String?, language: McsLanguage, firstname: String, lastname: String, gender: McsGender, dob: Date, country: String, state: String, city: String, line: String, longitude: Double?, latitude: Double?): McsPatient {
            return McsPatient(username, image, language, McsProfile.create(firstname, lastname, gender, dob), McsAddress.create(country, state, city, line, longitude, latitude))
        }
        fun update(firstname: String, lastname: String, gender: McsGender, dob: Date, country: String, state: String, city: String, line: String, longitude: Double?, latitude: Double?): McsPatient {
            return McsPatient(null, null, null, McsProfile.create(firstname, lastname, gender, dob), McsAddress.create(country, state, city, line, longitude, latitude))
        }
        fun partialUpdate(image: String?, language: McsLanguage?): McsPatient {
            return McsPatient(null, image, language, null, null)
        }
    }

}