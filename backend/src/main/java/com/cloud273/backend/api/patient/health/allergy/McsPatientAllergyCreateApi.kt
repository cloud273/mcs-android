package com.cloud273.backend.api.patient.health.allergy

import com.cloud273.backend.api.base.McsCreateApi
import com.cloud273.backend.model.patient.McsAllergy

// Error description
// 403 Invalid/Expired token

class McsPatientAllergyCreateApi(token: String, allergy: McsAllergy) : McsCreateApi("patient/health/allergy", token, allergy)