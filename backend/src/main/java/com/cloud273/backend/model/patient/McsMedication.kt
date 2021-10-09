package com.cloud273.backend.model.patient

import com.cloud273.backend.model.common.McsBase
import com.cloud273.backend.model.common.McsMedicationType
import com.cloud273.backend.model.common.McsTrilean
import com.google.gson.annotations.SerializedName

class McsMedication(var name: McsMedicationType?,
                    var value: McsTrilean,
                    @SerializedName("description") var note: String?) : McsBase() {

    companion object {
        fun update(id: Int, value : McsTrilean, note : String?): McsMedication {
            val result = McsMedication(null, value, note)
            result.id = id
            return result
        }
    }

}