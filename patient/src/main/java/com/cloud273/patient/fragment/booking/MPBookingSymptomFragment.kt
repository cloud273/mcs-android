package com.cloud273.patient.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.patient.health.McsPatientHealthDetailApi
import com.cloud273.backend.model.patient.McsSymptom
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.mcs.textController.McsEditTextControllerSymptom
import com.cloud273.mcs.textController.McsEditTextControllerSymptomWhen
import com.cloud273.patient.activity.MPBookingActivity
import com.cloud273.patient.center.MPDatabase
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_booking_symptom.*

class MPBookingSymptomFragment: McsFragment() {

    private lateinit var appointment: MPAppointment

    private lateinit var nameEditTextController: McsEditTextControllerSymptom
    private lateinit var noteEditTextController: McsEditTextControllerSymptomWhen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val owner = activity as MPBookingActivity
        appointment = owner.appointment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_symptom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Symptom"
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        nameEditTextController = McsEditTextControllerSymptom(nameLayout)
        noteEditTextController = McsEditTextControllerSymptomWhen(noteLayout)
        continueBtn.setOnClickListener {
            next()
            return@setOnClickListener
        }
    }

    private fun next() {
        dismissFocusIfNeed()
        nameEditTextController.showErrorIfNeed()
        noteEditTextController.showErrorIfNeed()
        if (nameEditTextController.isValid && noteEditTextController.isValid) {
            instanceDatabase.token?.also { token ->
                McsPatientHealthDetailApi(token).run { success, allergies, surgeries, medications, code ->
                    if (success) {
                        MPDatabase.instance.updateAccount(allergies!!, surgeries!!, medications!!)
                        if (isResumed) {
                            val name = nameEditTextController.value as String
                            val note = noteEditTextController.value as String
                            val symptom = McsSymptom.create(name, note)
                            appointment.set(listOf(symptom))
                            val bundle = Bundle()
                            bundle.putString(MPBookingHealthFragment.appointmentKey, SupportCenter.instance.push(appointment))
                            navigate(R.id.bookingHealthFragment, bundle)
                        }
                    } else {
                        if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

}
