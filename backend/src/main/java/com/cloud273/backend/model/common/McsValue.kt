package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName

open class McsValue(var code: Any,
                    @SerializedName("name") var nameMap: Map<String, String>)