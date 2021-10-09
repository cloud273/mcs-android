package com.cloud273.backend.api.patient.appointment

import com.cloud273.backend.api.base.McsAppointmentStatusUpdateApi

// Error description
// 403 Invalid/Expired token
// 404 Not found
// 406 Cannot be cancelled
class McsPatientCancelAppointmentApi (token: String, id: Int, note: String) : McsAppointmentStatusUpdateApi("patient/appointment/cancel", token, id, note)