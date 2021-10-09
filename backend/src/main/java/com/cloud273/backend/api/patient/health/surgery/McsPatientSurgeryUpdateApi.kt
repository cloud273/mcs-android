package com.cloud273.backend.api.patient.health.surgery

import com.cloud273.backend.api.base.McsUpdateApi
import com.cloud273.backend.model.patient.McsSurgery

// Error description
// 403 Invalid/Expired token
// 404 Not found
class McsPatientSurgeryUpdateApi(token: String, surgery: McsSurgery) : McsUpdateApi("patient/health/surgery", token, surgery)