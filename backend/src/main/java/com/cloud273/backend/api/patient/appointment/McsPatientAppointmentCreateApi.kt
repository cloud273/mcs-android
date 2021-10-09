package com.cloud273.backend.api.patient.appointment

import com.cloud273.backend.api.base.McsCreateApi
import com.cloud273.backend.model.common.McsAppointment

class McsPatientAppointmentCreateApi(token: String, appointment: McsAppointment) : McsCreateApi("patient/appointment", token, appointment)