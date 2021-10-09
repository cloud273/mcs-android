package com.cloud273.backend.api.patient.booking

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.backend.model.patient.McsSymptom
import com.dungnguyen.qdcore.extension.base64Encode
import com.dungnguyen.qdcore.extension.toArray
import com.dungnguyen.qdcore.extension.toJsonString
import kotlinx.coroutines.Dispatchers

class McsPatientListSpecialtyApi(token: String, private val symptoms: List<McsSymptom>): McsUserRequestApi(token) {

    override fun api() : String {
        return "patient/booking/specialty/list?symptoms=${symptoms.toJsonString()!!.base64Encode()}"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, specialties: List<McsSpecialty>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<McsSpecialty>(McsSpecialty::class.java)
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