package com.cloud273.backend.api.doctor.`package`

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsPackage
import com.dungnguyen.qdcore.extension.toArray
import kotlinx.coroutines.Dispatchers

class McsDoctorListPackageApi(token: String): McsUserRequestApi(token) {

    override fun api() : String {
        return "doctor/package/list"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, packages: List<McsPackage>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<McsPackage>(McsPackage::class.java)
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