package com.cloud273.backend.api.patient.health.medication

import com.cloud273.backend.api.base.McsUpdateApi
import com.cloud273.backend.model.patient.McsMedication

// Error description
// 403 Invalid/Expired token
// 404 Not found
class McsPatientMedicationUpdateApi(token: String, medication: McsMedication) : McsUpdateApi("patient/health/medication", token, medication)