package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsCreateApi
import com.cloud273.backend.model.patient.McsPatient

// Error description
// 401 Invalid email or phone
// 403 Invalid/Expired token
// 409 Existed email or phone
class McsPatientCreateApi(token: String, patient: McsPatient) : McsCreateApi("patient", token, patient)