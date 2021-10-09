package com.cloud273.mcs.model

import com.cloud273.backend.model.common.*
import com.cloud273.backend.model.doctor.McsClinicCert
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.backend.model.doctor.McsDoctorCert
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.backend.model.patient.*
import com.cloud273.mcs.center.McsAppConfiguration
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.util.toAppMonthYearString
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.add
import com.dungnguyen.qdcore.extension.toArray
import com.dungnguyen.qdcore.extension.toCurrency
import com.dungnguyen.qdcore.extension.toJsonString
import com.dungnguyen.qdcore.model.TextInterface
import java.lang.reflect.Type
import java.util.*

val McsAddress.country: McsCountry
    get() = McsAppConfiguration.instance.country(countryCode)!!

val McsAddress.state: McsState
    get() = McsAppConfiguration.instance.state(stateCode)!!

val McsAddress.city: McsCity
    get() = McsAppConfiguration.instance.city(cityCode)!!

fun McsAddress.getString(): String {
    return "${line}, ${city.name}, ${state.name}"
}

val McsAppointment.specialty: McsSpecialty
    get() = McsAppConfiguration.instance.specialty(specialtyCode)!!

val McsAppointment.acceptable: Boolean?
    get() {
        return if (status != null) {
            status!!.value == McsAptStatusType.created && begin.add(second = McsAppConfiguration.instance.acceptableEnd) > today
        } else {
            null
        }
    }

val McsAppointment.rejectable: Boolean?
    get() {
        return if (status != null) {
            status!!.value == McsAptStatusType.created
                    && begin.add(second = McsAppConfiguration.instance.rejectableEnd) > today
        } else {
            null
        }
    }

val McsAppointment.cancelable: Boolean?
    get() {
        return if (status != null) {
            (status!!.value == McsAptStatusType.created || status!!.value == McsAptStatusType.accepted)
                    && begin.add(second = McsAppConfiguration.instance.cancelableEnd) > today
        } else {
            null
        }
    }

val McsAppointment.beginable: Boolean?
    get() {
        return if (status != null) {
            status!!.value == McsAptStatusType.accepted
                    && begin.add(second = McsAppConfiguration.instance.beginableEnd) > today
                    && begin.add(second = McsAppConfiguration.instance.beginableFrom) < today
        } else {
            null
        }
    }

val McsAppointment.finishable: Boolean?
    get() {
        return if (status != null) {
            status!!.value == McsAptStatusType.started
                    && begin.add(second = McsAppConfiguration.instance.finishableEnd) > today
        } else {
            null
        }
    }

private val today: Date
    get() = Date()

fun McsAptStatusType.getString(): String {
    return when (this) {
        McsAptStatusType.created -> "Created_status".localized
        McsAptStatusType.accepted -> "Accepted_status".localized
        McsAptStatusType.rejected -> "Rejected_status".localized
        McsAptStatusType.cancelled -> "Cancelled".localized
        McsAptStatusType.started -> "In_progress".localized
        else -> "Finished".localized
    }
}

val McsCertificate.title: String
    get() {
        var result = ""
        when (this) {
            is McsClinicCert -> {
                result = title
            }
            is McsDoctorCert -> {
                result = title
            }
        }
        return result
    }

val McsCity.name: String
    get() = nameMap[instanceDatabase.language.value]!!

val McsClinicCert.title: String
    get() = type.getString()

fun McsClinicCertType.getString(): String {
    return when (this) {
        McsClinicCertType.working -> "Clinic_working_certificate".localized
        else -> "Clinic_other_certificate".localized
    }
}

val McsCountry.name: String
    get() = nameMap[instanceDatabase.language.value]!!

fun McsDoctor.specialtiesString(preferSpecialtyCode: String?): String {
    var result = ""
    if (specialtyCodes != null) {
        for (code in specialtyCodes!!) {
            val specialty = McsAppConfiguration.instance.specialty(code)
            if (specialty != null) {
                if (code == preferSpecialtyCode) {
                    result = if (result.isNotEmpty()) {
                        specialty.name + ", " + result
                    } else {
                        specialty.name
                    }
                } else {
                    if (result.isNotEmpty()) {
                        result += ", "
                    }
                    result += specialty.name
                }
            }
        }
    }
    return result
}

val McsDoctorCert.title: String
    get() = type.getString()

fun McsDoctorCertType.getString(): String {
    return when (this) {
        McsDoctorCertType.personal -> "ID".localized
        McsDoctorCertType.working -> "Doctor_working_certificate".localized
        McsDoctorCertType.degree -> "Degree".localized
        else -> "Doctor_other_certificate".localized
    }
}

fun McsGender.getString(): String {
    return when (this) {
        McsGender.male -> "Male".localized
        else -> "Female".localized
    }
}

val McsMedicationType.order: Int
    get() {
        return when (this) {
            McsMedicationType.highBP -> 1
            McsMedicationType.highCholesterol -> 2
            McsMedicationType.pregnant -> 3
            else -> 4
        }
    }

fun McsMedicationType.getString(): String {
    return when (this) {
        McsMedicationType.highBP -> "High_bp".localized
        McsMedicationType.highCholesterol -> "High_cholesterol".localized
        McsMedicationType.pregnant -> "Pregnant".localized
        else -> "Cancer".localized
    }
}

fun McsNotifyType.getString(): String {
    return when (this) {
        McsNotifyType.sms -> "sms".localized
        else -> "email".localized
    }
}

val McsPackage.specialty: McsSpecialty
    get() = McsAppConfiguration.instance.specialty(specialtyCode)!!

val McsPackage.durationString: String
    get() = (visitTime / 60).toString() + " " + "Minute_unit".localized

fun McsPackageType.getString(): String {
    return when (this) {
        McsPackageType.telemed -> "Telemedicine".localized
        else -> "Exam_at_clinic".localized
    }
}

val McsPatient.medications: List<McsMedication>?
    get() {
        var result: MutableList<McsMedication>? = null
        if (_medications != null) {
            result = mutableListOf()
            for (obj in _medications!!) {
                if (profile!!.gender == McsGender.female || obj.name != McsMedicationType.pregnant) {
                    result.add(obj)
                }
            }
        }
        result?.sortWith { t1, t2 ->
            t1.name!!.order - t2.name!!.order
        }
        return result
    }

fun McsPrice.getString(): String {
    val locale: Locale = when (currency) {
        McsCurrency.vnd -> {
            Locale.forLanguageTag("vi-VN")
        }
        McsCurrency.usd -> {
            Locale.forLanguageTag("en-US")
        }
        else -> {
            Locale.getDefault()
        }
    }
    return amount.toCurrency(locale)
}

val McsProfile.fullName: String
    get() {
        var result = ""
        if (firstname.isNotEmpty()) {
            result += firstname
        }
        if (lastname.isNotEmpty()) {
            if (result.isNotEmpty()) {
                result += " "
            }
            result += lastname
        }
        return result
    }

val McsSpecialty.name: String
    get() = nameMap[instanceDatabase.language.value]!!

val McsState.name: String
    get() = nameMap[instanceDatabase.language.value]!!

fun McsTrilean.getString(): String {
    return when (this) {
        McsTrilean.yes -> "Yes".localized
        McsTrilean.no -> "No".localized
        else -> "Unknown".localized
    }
}

fun McsUserType.getString(): String {
    return when (this) {
        McsUserType.patient -> "Patient".localized
        McsUserType.clinic -> "Clinic".localized
        McsUserType.doctor -> "Doctor".localized
        else -> "System".localized
    }
}

val McsValue.name: String
    get() = nameMap[instanceDatabase.language.value]!!

fun List<Any>.getString(): String {
    var result = ""
    for (item in this) {
        if (result.isNotEmpty()) {
            result += "\n"
        }
        when (item) {
            is McsSymptom -> {
                result += "+ ${item.name}: ${item.note}"
            }
            is McsAllergy -> {
                result += if (item.note.isNullOrEmpty()) {
                    "+ ${item.name}"
                } else {
                    "+ ${item.name}: ${item.note}"
                }
            }
            is McsSurgery -> {
                result += if (item.note.isNullOrEmpty()) {
                    "+ ${item.name}(${item.date.toAppMonthYearString()})"
                } else {
                    "+ ${item.name}(${item.date.toAppMonthYearString()}): ${item.note}"
                }
            }
            is McsMedication -> {
                result += "+ ${item.name!!.getString()}(${item.value.getString()})"
            }
            is TextInterface -> {
                result += "+ ${item.getText()})"
            }
            is String -> {
                result += "+ ${item})"
            }
        }
    }

    return result
}

fun<T: McsBase> List<T>.clone(type: Type, withoutID: Boolean): List<T> {
    val result = this.toJsonString()!!.toArray<T>(type)!!
    if (withoutID) {
        for (item in result) {
            item.id = null
        }
    }
    return result
}
