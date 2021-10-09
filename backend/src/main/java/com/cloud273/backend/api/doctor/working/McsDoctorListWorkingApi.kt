package com.cloud273.backend.api.doctor.working

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.doctor.McsWorkingTime
import com.cloud273.backend.util.toApiDateTimeString
import com.dungnguyen.qdcore.extension.encodeQuery
import com.dungnguyen.qdcore.extension.toArray
import kotlinx.coroutines.Dispatchers
import java.util.*

class McsDoctorListWorkingApi(token: String, private val from: Date, private val to: Date): McsUserRequestApi(token) {

    override fun api() : String {
        return "doctor/working-time/list?from=${from.toApiDateTimeString().encodeQuery()}&to=${to.toApiDateTimeString().encodeQuery()}"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, workingTimes: List<McsWorkingTime>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<McsWorkingTime>(McsWorkingTime::class.java)
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