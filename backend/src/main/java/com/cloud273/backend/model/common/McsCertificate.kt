package com.cloud273.backend.model.common

import com.cloud273.backend.imageUrl
import com.cloud273.backend.util.DayTypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

open class McsCertificate(var code: String,
                          var name: String,
                          var issuer: String,
                          @JsonAdapter(DayTypeAdapter::class) var issueDate: Date,
                          @JsonAdapter(DayTypeAdapter::class) var expDate: Date?,
                          @SerializedName("image") var imageName: String?) : McsBase() {

    val image: String?
        get() {
            return if (imageName != null) {
                imageUrl + imageName
            } else {
                null
            }
        }

}