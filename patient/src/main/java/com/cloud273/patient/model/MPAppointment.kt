package com.cloud273.patient.model

import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.backend.model.common.McsPrice
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.backend.model.patient.*
import com.cloud273.mcs.center.McsAppConfiguration
import java.util.*

class MPAppointment(var begin: Date? = null,
                    var specialtyCode: String? = null,
                    var type: McsPackageType? = null,
                    var price: McsPrice? = null,
                    var visitTime: Int? = null,
                    var symptoms: List<McsSymptom>? = null,
                    var allergies: List<McsAllergy>? = null,
                    var surgeries: List<McsSurgery>? = null,
                    var medications: List<McsMedication>? = null,
                    var packageId: Int? = null) {

    companion object {
        fun create(type: McsPackageType): MPAppointment {
            return MPAppointment(type = type)
        }
    }

    val specialty: McsSpecialty
        get() = McsAppConfiguration.instance.specialty(specialtyCode!!)!!

    fun set(symptoms: List<McsSymptom>) {
        this.symptoms = symptoms
    }

    fun set(allergies: List<McsAllergy>, surgeries: List<McsSurgery>, medications: List<McsMedication>) {
        this.allergies = allergies
        this.surgeries = surgeries
        this.medications = medications
    }

    fun set(pack: McsPackage) {
        this.packageId = pack.id!!
        this.price = pack.price
        this.visitTime = pack.visitTime
    }

    fun set(specialtyCode: String) {
        this.specialtyCode = specialtyCode
    }

    fun set(begin: Date) {
        this.begin = begin
    }

    fun toAppointment(): McsAppointment {
        return McsAppointment(begin!!, specialtyCode!!, type!!, price!!, visitTime!!, symptoms!!, allergies, surgeries, medications, packageId!!)
    }

}