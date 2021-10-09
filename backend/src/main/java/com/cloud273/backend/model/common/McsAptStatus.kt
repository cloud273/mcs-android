package com.cloud273.backend.model.common

import com.google.gson.annotations.SerializedName

class McsAptStatus(var by: McsUserType,
                   var value: McsAptStatusType,
                   @SerializedName("description") var note: String?) : McsBase() {

}