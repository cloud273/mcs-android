package com.cloud273.backend.api.doctor.account

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsClinic
import com.cloud273.backend.model.doctor.McsDoctor
import com.dungnguyen.qdcore.extension.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers

class McsDoctorClinicInfoApi(token: String) : McsUserRequestApi(token) {

    private data class Output(val doctor: McsDoctor, val clinic: McsClinic)

    override fun api() : String {
        return "doctor/info"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, doctor: McsDoctor?, clinic: McsClinic?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.doctor, data.clinic, code)
                } else {
                    completion(false, null, null, 500)
                }
            } else {
                completion(false, null, null, code)
            }
        }
    }

}