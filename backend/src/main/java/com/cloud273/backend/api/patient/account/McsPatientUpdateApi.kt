package com.cloud273.backend.api.patient.account

import com.cloud273.backend.api.base.McsUpdateApi
import com.cloud273.backend.model.patient.McsPatient


class McsPatientUpdateApi(token: String, patient: McsPatient) : McsUpdateApi("patient", token, patient)