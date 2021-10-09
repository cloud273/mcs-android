package com.cloud273.backend.model.common

import com.cloud273.backend.resourceUrl
import com.google.gson.annotations.SerializedName

class McsSpecialty (var code: String,
                    @SerializedName("image") var imageName: String?,
                    @SerializedName("name") var nameMap: Map<String, String>) {
    val image: String
        get() = "$resourceUrl$imageName"
}
