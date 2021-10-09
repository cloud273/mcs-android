package com.cloud273.doctor.fragment.appointment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import com.cloud273.backend.api.doctor.appointment.McsDoctorFinishAppointmentApi
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_prescription.*

class MDPrescriptionFragment: McsFragment() {
    companion object {
        const val appointmentKey = "appointment"
    }

    private lateinit var appointment: McsAppointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_md_prescription, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Prescription"
        finishBtn.setOnClickListener {
            requireContext().showAlert(title = null, message = "Finish_appointment_message".localized, no = "No".localized, yes = "Agree".localized) {
                // Error description
                // 403 Invalid/Expired token
                // 404 Not found
                // 406 Cannot be started
                McsDoctorFinishAppointmentApi(MDDatabase.instance.token!!, appointment.id!!, note = "Finished by doctor").run { success, _, code ->
                    if (success) {
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                            Intent(appointmentDidUpdatedNotification)
                        )
                        findNavController().popBackStack(R.id.mdListAppointmentFragment, true)
                    } else {
                        if (code == 404) {
                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                Intent(appointmentDidUpdatedNotification)
                            )
                            requireContext().showAlert(title = "Error".localized, message = "Not_found_appointment_message".localized, close = "Close".localized) {
                                findNavController().popBackStack(R.id.mdListAppointmentFragment, true)
                            }
                        } else if (code == 406) {
                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                Intent(appointmentDidUpdatedNotification)
                            )
                            refresh()
                            requireContext().showAlert(title = "Error".localized, message = "No_finishable_appointment_message".localized, close = "Close".localized)
                        }
                    }
                }
            }
        }
    }
}