package com.cloud273.patient.fragment.booking

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cloud273.backend.api.patient.appointment.McsPatientAppointmentCreateApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.util.titleContentCell
import com.cloud273.patient.R
import com.cloud273.patient.center.MPDatabase
import com.cloud273.patient.model.McsRecyclerRender
import com.cloud273.patient.view.MPAddressClinicCell
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.view.RecyclerCircleImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.mcs.center.moveToAppointmentListNotification
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.name
import com.cloud273.mcs.util.titleContentHighlightCell
import com.cloud273.mcs.util.toAppDateTimeString
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import kotlinx.android.synthetic.main.fragment_mp_booking_summary.*

class MPBookingSummaryFragment: McsFragment(), RecyclerCellInterface {

    companion object {
        const val doctorKey = "doctor"
        const val appointmentKey = "appointment"
    }


    private val adapter = RecyclerSectionAdapter(McsRecyclerRender(), this)

    private lateinit var doctor: McsDoctor
    private lateinit var appointment: MPAppointment

    private var agree = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
            doctor = SupportCenter.instance.pop(it.getString(doctorKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        infoBtn.setOnClickListener {

        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            agree = isChecked
            reloadBookButton()
        }
        bookBtn.setOnClickListener {
            book()
        }
        reloadView()
    }

    private fun reloadView() {
        val sections = mutableListOf<RecyclerSectionData>()
        sections.add(RecyclerSectionData(cells = listOf(RecyclerCellData(data = ImageModel(doctor.image, R.mipmap.doctor_icon), resource = RecyclerCircleImageCell.cellId))))

        val bookingCells = mutableListOf<RecyclerCellData>()
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Doctor".localized, doctor.profile!!.fullName), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Specialty".localized, appointment.specialty.name), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        bookingCells.add(RecyclerCellData(data = doctor.clinic!!, resource = MPAddressClinicCell.lineHighlightCellId))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Time".localized, appointment.begin!!.toAppDateTimeString()), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Price".localized, appointment.price!!.getString()), resource = RecyclerTextDetailCell.titleContentHighlightCell))

        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Booking_information".localized), bookingCells))

        val healthCells = mutableListOf<RecyclerCellData>()
        healthCells.add(RecyclerCellData(data = TextDetailModel("Symptom".localized, appointment.symptoms!!.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        appointment.allergies?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Allergy".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }
        appointment.surgeries?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Surgery".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }
        appointment.medications?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Medication".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }

        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Health_information".localized), healthCells))
        recyclerView.unDrawDividerAt(0)
        adapter.setData(sections)
        reloadBookButton()
    }

    private fun reloadBookButton () {
        bookBtn.isEnabled = agree
        if (agree) {
            bookBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            bookBtn.setBackgroundColor(Color.DKGRAY)
        }
    }

    private fun book() {
        MPDatabase.instance.token?.also { token ->
            McsPatientAppointmentCreateApi(token, appointment.toAppointment()).run { success, _, code ->
                if (isResumed) {
                    if (success) {
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent(appointmentDidUpdatedNotification))
                        context?.showAlert("Successful".localized, null, "Close".localized) {
                            activity?.finish()
                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent(moveToAppointmentListNotification))
                        }
                    } else {
                        if (code == 406) {
                            context?.showAlert(null, "Unavailable_doctor_message".localized, "Close".localized) {
                                activity?.finish()
                            }
                        } else if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

}