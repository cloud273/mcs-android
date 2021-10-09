package com.cloud273.patient.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.booking.McsPatientListSpecialtyApi
import com.cloud273.backend.model.patient.McsAllergy
import com.cloud273.backend.model.patient.McsMedication
import com.cloud273.patient.R
import com.dungnguyen.qdcore.recycler.common.RecyclerCellData
import com.dungnguyen.qdcore.recycler.common.RecyclerHeaderFooterData
import com.dungnguyen.qdcore.recycler.common.RecyclerSectionData
import com.dungnguyen.qdcore.recycler.view.RecyclerCheckCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.backend.model.patient.McsSurgery
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.clone
import com.cloud273.mcs.model.medications
import com.cloud273.patient.fragment.health.MPHealthProfileFragment
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import kotlinx.android.synthetic.main.fragment_mp_booking_health.*

class MPBookingHealthFragment: MPHealthProfileFragment() {

    companion object {
        const val appointmentKey = "appointment"
    }

    lateinit var appointment: MPAppointment

    private var isOn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_health, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Health_information"
        nextBtn.setOnClickListener {
            next()
        }
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun recyclerView(): RecyclerView {
        return recyclerView
    }

    override fun navigationAllergyId(): Int {
        return R.id.allergyFragment
    }

    override fun navigationSurgeryId(): Int {
        return R.id.surgeryFragment
    }

    override fun sessions(): MutableList<RecyclerSectionData> {
        val sections = mutableListOf<RecyclerSectionData>()
        sections.add(
            RecyclerSectionData(
                RecyclerHeaderFooterData("Share".localized.uppercase()),
                listOf(
                    RecyclerCellData("share", RecyclerCheckCell.Model("Booking_health_message".localized, isOn), RecyclerCheckCell.cellId)
                )
            )
        )
        if (isOn) {
            sections.addAll(super.sessions())
        }
        return sections
    }

    override fun onSwitchChange(cell: RecyclerCheckCell, id: Any?, value: Boolean) {
        if (id == "share") {
            isOn = value
            reloadView()
        } else {
            super.onSwitchChange(cell, id, value)
        }
    }

    private fun next() {
        instanceDatabase.token?.also { token ->
            McsPatientListSpecialtyApi(token, appointment.symptoms!!).run { success, specialties, code ->
                if (isResumed) {
                    if (success) {
                        val bundle = Bundle()
                        if (isOn) {
                            val patient = instanceDatabase.account as McsPatient
                            appointment.set(
                                patient.allergies!!.clone(McsAllergy::class.java, true),
                                patient.surgeries!!.clone(McsSurgery::class.java, true),
                                patient.medications!!.clone(McsMedication::class.java, true))
                        }
                        bundle.putString(MPBookingSpecialtyFragment.appointmentKey, SupportCenter.instance.push(appointment))
                        bundle.putString(MPBookingSpecialtyFragment.specialtiesKey, SupportCenter.instance.push(specialties!!))
                        navigate(R.id.bookingSpecialtyFragment, bundle)
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