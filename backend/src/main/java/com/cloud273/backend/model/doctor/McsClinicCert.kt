package com.cloud273.backend.model.doctor

import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.backend.model.common.McsClinicCertType
import java.util.*

class McsClinicCert(code: String,
                    name: String,
                    issuer: String,
                    issuerDate: Date,
                    expDate: Date?,
                    imageName: String?,
                    var type: McsClinicCertType) : McsCertificate(code, name, issuer, issuerDate, expDate, imageName) {

}