package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName

class McsCountry(var code: String,
                 @SerializedName("name") var nameMap: Map<String, String>,
                 @SerializedName("state") var states: List<McsState>) {

}