package com.cloud273.backend.api.doctor.clinic

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsClinic
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsDoctorClinicDetailApi(token: String) : McsUserRequestApi(token) {

    override fun api() : String {
        return "doctor/clinic"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, clinic: McsClinic?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<McsClinic>(McsClinic::class.java)
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