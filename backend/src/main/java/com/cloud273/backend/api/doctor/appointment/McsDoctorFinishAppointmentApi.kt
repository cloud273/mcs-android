package com.cloud273.backend.api.doctor.appointment

import com.cloud273.backend.api.base.McsAppointmentStatusUpdateApi

// Error description
// 403 Invalid/Expired token
// 404 Not found
// 406 Cannot be finished

class McsDoctorFinishAppointmentApi (token: String, id: Int, note: String) : McsAppointmentStatusUpdateApi("doctor/appointment/finish", token, id, note)