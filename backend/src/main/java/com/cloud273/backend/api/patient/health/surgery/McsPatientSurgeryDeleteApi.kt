package com.cloud273.backend.api.patient.health.surgery

import com.cloud273.backend.api.base.McsDeleteApi

// Error description
// 403 Invalid/Expired token
// 404 Not found
class McsPatientSurgeryDeleteApi(token: String, id: Int) : McsDeleteApi("patient/health/surgery?id=${id}", token)