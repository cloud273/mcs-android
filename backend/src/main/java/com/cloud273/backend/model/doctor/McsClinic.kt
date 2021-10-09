package com.cloud273.backend.model.doctor

import com.cloud273.backend.imageUrl
import com.cloud273.backend.model.common.McsAddress
import com.cloud273.backend.model.common.McsBase
import com.google.gson.annotations.SerializedName

class McsClinic(var name: String,
                @SerializedName("image") var imageName: String?,
                var email: String,
                var phone: String,
                var workPhone: String?,
                var address: McsAddress,
                var certificates: List<McsClinicCert>?) : McsBase() {

    val image: String?
        get() {
            return if (imageName != null) {
                imageUrl + imageName
            } else {
                null
            }
        }

}