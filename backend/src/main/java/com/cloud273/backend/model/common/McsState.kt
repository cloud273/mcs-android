package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName

class McsState(var code: String,
               @SerializedName("name") var nameMap: Map<String, String>,
               @SerializedName("city") var cities: List<McsCity>) {

}