package com.cloud273.backend.api.patient.appointment

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsAptStatusType
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.backend.util.toApiDateTimeString
import com.dungnguyen.qdcore.extension.base64Encode
import com.dungnguyen.qdcore.extension.encodeQuery
import com.dungnguyen.qdcore.extension.toArray
import com.dungnguyen.qdcore.extension.toJsonString
import kotlinx.coroutines.Dispatchers
import java.util.*

class McsPatientListAppointmentApi(token: String, private val type: McsPackageType, private val statuses: List<McsAptStatusType>? = null, private val from: Date, private val to: Date): McsUserRequestApi(token) {

    override fun api() : String {
        var result = "patient/appointment/list?from=${from.toApiDateTimeString().encodeQuery()}&to=${to.toApiDateTimeString().encodeQuery()}"
        type.also {
            result += "&type=${type.value}"
        }
        if (statuses != null && statuses!!.isNotEmpty()) {
            val jsonStatus = mutableListOf<String>()
            for (status in statuses) {
                jsonStatus.add(status.value)
            }
            result += "&statusTypes=${jsonStatus.toJsonString()!!.base64Encode()}"
        }
        return result
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, appointments: List<McsAppointment>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<McsAppointment>(McsAppointment::class.java)
                if (data != null) {
                    completion(true, data, code)
                } else {
                    completion(false, null, 500)
                }
            } else {
                completion(false, null, code)
            }
        }
    }

}