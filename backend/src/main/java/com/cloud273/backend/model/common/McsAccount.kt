package com.cloud273.backend.model.common

import com.cloud273.backend.imageUrl
import com.google.gson.annotations.SerializedName

open class McsAccount(var username: String?,
                      @SerializedName("image") var imageName: String?,
                      var language: McsLanguage?,
                      var profile: McsProfile?) : McsBase() {
    val image: String
        get() = "$imageUrl$imageName"
}