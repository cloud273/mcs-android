package com.cloud273.backend.api.patient.health

import com.cloud273.backend.api.base.McsUserRequestApi
import com.cloud273.backend.model.patient.McsAllergy
import com.cloud273.backend.model.patient.McsMedication
import com.cloud273.backend.model.patient.McsSurgery
import com.dungnguyen.qdcore.extension.toObject
import kotlinx.coroutines.Dispatchers

class McsPatientHealthDetailApi(token: String): McsUserRequestApi(token) {

    private data class Output(val allergies: List<McsAllergy>, val surgeries: List<McsSurgery>, val medications: List<McsMedication>)

    override fun api() : String {
        return "patient/health"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    // Error description
    // 403 Invalid/Expired token
    fun run(completion: (success: Boolean, allergies: List<McsAllergy>?, surgeries: List<McsSurgery>?, medications: List<McsMedication>?, code: Int) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.allergies, data.surgeries, data.medications, code)
                } else {
                    completion(false, null, null, null, 500)
                }
            } else {
                completion(false, null, null, null, code)
            }
        }
    }

}