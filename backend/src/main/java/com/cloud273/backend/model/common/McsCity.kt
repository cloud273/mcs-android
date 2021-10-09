package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName

class McsCity(var code: String,
              @SerializedName("name") var nameMap: Map<String, String>) {

}