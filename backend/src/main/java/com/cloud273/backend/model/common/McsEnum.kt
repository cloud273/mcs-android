package com.cloud273.backend.model.common

enum class McsAccountType (val value: String) {
    patient ("patient"),
    doctor ("doctor")
}

enum class McsAptStatusType (val value: String) {
    created ("created"),
    accepted ("accepted"),
    started ("started"),
    rejected ("rejected"),
    cancelled ("cancelled"),
    finished ("finished")
}

enum class McsClinicCertType (val value: String) {
    working ("working"),
    other ("other")
}

enum class McsCurrency (val value: String) {
    vnd ("vnd"),
    usd ("usd")
}

enum class McsDoctorCertType (val value: String) {
    personal ("personal"),
    working ("working"),
    degree ("degree"),
    other ("other")
}

enum class McsGender (val value: String) {
    male ("male"),
    female ("female")
}

enum class McsLanguage (val value: String) {
    vi ("vi"),
    en ("en")
}

enum class McsMedicationType(val value: String) {
    highBP ("highBP"),
    highCholesterol ("highCholesterol"),
    pregnant ("pregnant"),
    cancer ("cancer")
}

enum class McsNotifyType (val value: String) {
    email ("email"),
    sms ("sms")
}

enum class McsPackageType (val value: String) {
    classic ("classic"),
    telemed ("telemed")
}

enum class McsTrilean (val value: String) {
    yes ("yes"),
    no ("no"),
    unknown ("unknown")

}

enum class McsUserType (val value: String)  {
    patient ("patient"),
    clinic ("clinic"),
    doctor ("doctor"),
    system ("system")
}
