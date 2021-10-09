package com.cloud273.backend.api.patient.booking

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.util.toApiDateTimeString
import com.dungnguyen.qdcore.extension.encodeQuery
import com.dungnguyen.qdcore.extension.toArray
import kotlinx.coroutines.Dispatchers
import java.util.*

class McsPatientListBookingTimeApi(token: String, private val packageId: Int, private val from: Date, private val to: Date): McsUserRequestApi(token) {

    override fun api() : String {
       return "patient/booking/time/list?packageId=${packageId}&from=${from.toApiDateTimeString().encodeQuery()}&to=${to.toApiDateTimeString().encodeQuery()}"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    // 404 Not found packageId
    fun run(completion: (success: Boolean, times: List<String>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toArray<String>(String::class.java)
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