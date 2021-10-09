package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsPartialUpdateApi
import com.cloud273.backend.model.patient.McsPatient

class McsPatientPartialUpdateApi(token: String, patient: McsPatient) : McsPartialUpdateApi("patient", token, patient)