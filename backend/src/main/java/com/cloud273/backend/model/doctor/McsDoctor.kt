package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsAccount
import com.cloud273.backend.model.common.McsLanguage
import com.cloud273.backend.model.common.McsProfile
import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

class McsDoctor(username: String,
                imageName: String,
                language: McsLanguage,
                profile: McsProfile,
                var title: String,
                var biography: String?,
                var office: String?,
                var timezone: String?,
                @JsonAdapter(DayTypeAdapter::class) var startWork: Date?,
                var certificates: List<McsDoctorCert>?,
                @SerializedName("specialties") var specialtyCodes: List<String>?,
                var clinic: McsClinic?,
                var packages: List<McsPackage>?
                ) : McsAccount(username, imageName, language, profile) {

    var rating: Double = 4.5
    var noFb: Int = 125
    var favorite: Boolean = false
    var noApt: Int = 214

}