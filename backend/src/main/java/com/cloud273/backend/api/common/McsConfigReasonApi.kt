package com.cloud273.backend.api.common

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsValue
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsConfigReasonApi : McsRequestApi() {

    private data class Output(val patientCancel: List<McsValue>,
                              val clinicReject: List<McsValue>,
                              val systemReject: List<McsValue>)

    override fun api() : String {
        return "config/reasons"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    fun run(completion: (success: Boolean, patientCancel: List<McsValue>?, clinicReject: List<McsValue>?, systemReject: List<McsValue>?) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.patientCancel, data.clinicReject, data.systemReject)
                } else {
                    completion(false, null, null, null)
                }
            } else {
                completion(false, null, null, null)
            }
        }
    }

}