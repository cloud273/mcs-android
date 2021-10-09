package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.backend.model.common.McsDoctorCertType
import java.util.*

class McsDoctorCert(code: String,
                    name: String,
                    issuer: String,
                    issuerDate: Date,
                    expDate: Date?,
                    imageName: String?,
                    var type: McsDoctorCertType) : McsCertificate(code, name, issuer, issuerDate, expDate, imageName) {

}