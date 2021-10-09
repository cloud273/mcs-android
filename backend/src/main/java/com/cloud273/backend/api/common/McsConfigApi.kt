package com.cloud273.backend.api.common

import com.cloud273.backend.api.base.McsRequestApi
import com.cloud273.backend.model.common.McsCountry
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.backend.model.common.McsValue
import com.dungnguyen.qdcore.extension.toObject
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers

class McsConfigApi(): McsRequestApi() {

    private data class Duration(val from: Int,
                                val to: Int)

    private data class Reason(val patientCancel: List<McsValue>,
                              val clinicReject: List<McsValue>,
                              val systemReject: List<McsValue>)

    private data class AppointmentInfo(val creatable: Int,
                                       val acceptable: Int,
                                       val cancelable: Int,
                                       val rejectable: Int,
                                       val beginable: Duration,
                                       val finishable: Int)

    private data class Output(val countries: List<McsCountry>,
                              val specialties: List<McsSpecialty>,
                              val reasons: Reason,
                              @SerializedName("appointment") val appointmentInfo: AppointmentInfo)

    override fun api() : String {
        return "config"
    }

    override fun method(): HttpMethod {
        return HttpMethod.Get
    }

    fun run(completion: (success: Boolean, countries: List<McsCountry>?, specialties: List<McsSpecialty>?, patientCancel: List<McsValue>?, clinicReject: List<McsValue>?, systemReject: List<McsValue>?, creatableEnd: Int?, acceptableEnd: Int?, cancelableEnd: Int?, rejectableEnd: Int?, beginableFrom: Int?, beginableEnd: Int?, finishableEnd: Int?) -> Unit) {
        commonFetch(Dispatchers.Main) { code, _, body ->
            if (code == 200) {
                val data = body?.toObject<Output>(Output::class.java)
                if (data != null) {
                    completion(true, data.countries, data.specialties, data.reasons.patientCancel,
                        data.reasons.clinicReject, data.reasons.systemReject, data.appointmentInfo.creatable,
                        data.appointmentInfo.acceptable, data.appointmentInfo.cancelable, data.appointmentInfo.rejectable,
                        data.appointmentInfo.beginable.from, data.appointmentInfo.beginable.to, data.appointmentInfo.finishable)
                } else {
                    completion(false, null, null, null,
                        null, null, null,
                        null, null, null,
                        null, null, null)
                }
            } else {
                completion(false, null, null, null,
                    null, null, null,
                    null,null, null,
                    null, null, null)
            }
        }
    }

}