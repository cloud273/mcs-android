package com.cloud273.backend.api.patient.health.surgery

import com.cloud273.backend.api.base.McsCreateApi
import com.cloud273.backend.model.patient.McsSurgery

// Error description
// 403 Invalid/Expired token

class McsPatientSurgeryCreateApi(token: String, surgery: McsSurgery) : McsCreateApi("patient/health/surgery", token, surgery)