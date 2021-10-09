package com.cloud273.backend.api.doctor.account

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsDoctor
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsDoctorDetailApi (token: String) : McsUserRequestApi(token) {

    override fun api() : String {
        return "doctor"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, doctor: McsDoctor?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<McsDoctor>(McsDoctor::class.java)
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