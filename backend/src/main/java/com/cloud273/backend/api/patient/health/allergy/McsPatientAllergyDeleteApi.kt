package com.cloud273.backend.api.patient.health.allergy

import com.cloud273.backend.api.base.McsDeleteApi

// Error description
// 403 Invalid/Expired token
// 404 Not found
class McsPatientAllergyDeleteApi(token: String, id: Int) : McsDeleteApi("patient/health/allergy?id=${id}", token)