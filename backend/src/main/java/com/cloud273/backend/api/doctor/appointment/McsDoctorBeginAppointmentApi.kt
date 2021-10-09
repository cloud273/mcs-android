package com.cloud273.backend.api.doctor.appointment

import com.cloud273.backend.api.base.McsAppointmentStatusUpdateApi

// Error description
// 403 Invalid/Expired token
// 404 Not found
// 406 Cannot be started

class McsDoctorBeginAppointmentApi (token: String, id: Int, note: String) : McsAppointmentStatusUpdateApi("doctor/appointment/begin", token, id, note)