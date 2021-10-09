package com.cloud273.backend.api.patient.health.allergy

import com.cloud273.backend.api.base.McsUpdateApi
import com.cloud273.backend.model.patient.McsAllergy

// Error description
// 403 Invalid/Expired token
// 404 Not found
class McsPatientAllergyUpdateApi(token: String, allergy: McsAllergy) : McsUpdateApi("patient/health/allergy", token, allergy)