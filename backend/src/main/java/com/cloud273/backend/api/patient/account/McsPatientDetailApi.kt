package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.patient.McsPatient
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers


class McsPatientDetailApi(token: String) : McsUserRequestApi(token) {

    override fun api() : String {
        return "patient"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, patient: McsPatient?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<McsPatient>(McsPatient::class.java)
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