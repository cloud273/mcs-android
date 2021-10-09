package com.cloud273.backend.model.patient

import com.cloud273.backend.model.common.McsBase
import com.google.gson.annotations.SerializedName

class McsAllergy(var name: String,
                 @SerializedName("description") var note: String?) : McsBase() {

    companion object {

        fun create(name : String, note : String?): McsAllergy {
            return McsAllergy(name, note)
        }

        fun update(id: Int, name : String, note : String?): McsAllergy {
            val result = McsAllergy(name, note)
            result.id = id
            return result
        }

    }

}