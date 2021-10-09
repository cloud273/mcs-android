package com.cloud273.backend.model.common

import com.cloud273.backend.imageUrl
import com.cloud273.backend.model.doctor.McsClinic
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.backend.model.patient.*
import com.cloud273.backend.util.DateTimeTypeAdapter
import com.dungnguyen.qdcore.extension.add
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import java.util.*

class McsAppointment(@JsonAdapter(DateTimeTypeAdapter::class) val begin: Date,
                     @SerializedName("specialty") val specialtyCode: String,
                     val type: McsPackageType,
                     val price: McsPrice,
                     val visitTime: Int,
                     val symptoms: List<McsSymptom>,
                     val allergies: List<McsAllergy>?,
                     val surgeries: List<McsSurgery>?,
                     val medications: List<McsMedication>?,
                     val packageId: Int) : McsBase() {

    var status: McsAptStatus? = null
    var order: Int? = null
    var doctorInfo: McsDoctor? = null
    var clinicInfo: McsClinic? = null
    var patientInfo: McsPatient? = null

    companion object {
        fun create(begin: Date, specialtyCode: String, type: McsPackageType, price: McsPrice, visitTime: Int, symptoms: List<McsSymptom>, allergies: List<McsAllergy>?, surgeries: List<McsSurgery>?, medications: List<McsMedication>?, packageId: Int): McsAppointment {
            return McsAppointment(begin, specialtyCode, type, price, visitTime, symptoms, allergies, surgeries, medications, packageId)
        }
    }

}