package com.cloud273.backend.api.doctor.`package`

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsPackage
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsDoctorPackageDetailApi (token: String, private var id: Int) : McsUserRequestApi(token) {

    override fun api() : String {
        return "doctor/package?id=${id}"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    // 404 Not found
    fun run(completion: (success: Boolean, pack: McsPackage?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<McsPackage>(McsPackage::class.java)
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