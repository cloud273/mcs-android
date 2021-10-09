package com.cloud273.backend.api.patient.booking

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.common.*
import com.cloud273.backend.model.doctor.McsDoctor
import com.dungnguyen.qdcore.extension.toArray
import kotlinx.coroutines.Dispatchers


class McsPatientListDoctorApi(token: String, private val type: McsPackageType, private val specialtyCode: String): McsUserRequestApi(token) {

    override fun api() : String {
        return "patient/booking/doctor/list?type=${type.value}&specialty=${specialtyCode}"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, doctors: List<McsDoctor>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<McsDoctor>(McsDoctor::class.java)
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