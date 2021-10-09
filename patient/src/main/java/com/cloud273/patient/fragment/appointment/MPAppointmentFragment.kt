package com.cloud273.patient.fragment.appointment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cloud273.backend.api.common.McsConfigReasonApi
import com.cloud273.backend.api.patient.appointment.McsPatientCancelAppointmentApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.view.McsAppointmentStatusCell

import com.cloud273.patient.R
import com.cloud273.patient.model.McsRecyclerRender
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerButtonCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsUserType
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.*
import com.cloud273.mcs.util.*
import com.cloud273.patient.view.MPAddressClinicCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.unDrawDividerAt
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.model.TextModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.view.RecyclerCircleImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import kotlinx.android.synthetic.main.fragment_mp_appointment.*

class MPAppointmentFragment : McsFragment(), RecyclerCellInterface, RecyclerButtonCell.OnActionInterface, McsAppointmentStatusCell.OnActionInterface {

    companion object {
        const val appointmentKey = "appointment"
    }

    private val adapter = RecyclerSectionAdapter(McsRecyclerRender(), this)
    private lateinit var appointment: McsAppointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Detail"
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    private fun reloadView() {
        val unDrawAt = mutableListOf(0)
        val sections = mutableListOf<RecyclerSectionData>()
        sections.add(RecyclerSectionData(cells = listOf(RecyclerCellData(data = ImageModel(appointment.doctorInfo!!.image, R.mipmap.doctor_icon), resource = RecyclerCircleImageCell.cellId))))
        val bookingCells = mutableListOf<RecyclerCellData>()
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Time".localized, appointment.begin.toAppDateTimeString()), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        appointment.status?.also { status ->
            bookingCells.add(RecyclerCellData(data = McsAppointmentStatusCell.Model(status, status.by == McsUserType.patient || status.note.isNullOrEmpty()) , resource = McsAppointmentStatusCell.cellId))
        }
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Order".localized, appointment.order.toString()), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Doctor".localized, appointment.doctorInfo!!.profile!!.fullName), resource = RecyclerTextDetailCell.titleContentHighlightCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Specialty".localized, appointment.specialty.name), resource = RecyclerTextDetailCell.titleContentCell))
        bookingCells.add(RecyclerCellData(data = appointment.clinicInfo!!, resource = MPAddressClinicCell.cellId))
        bookingCells.add(RecyclerCellData(data = TextDetailModel("Price".localized, appointment.price.getString()), resource = RecyclerTextDetailCell.titleContentCell))

        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Booking_information".localized), bookingCells, null))

        val healthCells = mutableListOf<RecyclerCellData>()
        healthCells.add(RecyclerCellData(data = TextDetailModel("Symptom".localized, appointment.symptoms.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        appointment.allergies?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Allergy".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }
        appointment.surgeries?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Surgery".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }
        appointment.medications?.also {
            healthCells.add(RecyclerCellData(data = TextDetailModel("Medication".localized, it.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        }
        if (appointment.cancelable!!) {
            healthCells.add(RecyclerCellData("cancel", "Cancel".localized, RecyclerButtonCell.redButtonCell))
            unDrawAt.add(bookingCells.size + healthCells.size + 1)
        }

        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Health_information".localized), healthCells, null))

        recyclerView.unDrawDividerAt(unDrawAt)
        adapter.setData(sections)
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        super.onDrawCell(cell, id, data)
        if (id == "cancel") {
            val btnCell = (cell as RecyclerButtonCell)
            btnCell.listener = this
        } else if (id == "status") {
            val statusCell = (cell as McsAppointmentStatusCell)
            statusCell.listener = this
        }
    }

    override fun onInfoClick(cell: McsAppointmentStatusCell, id: Any?) {
        if (id == "status") {
            val status = appointment.status
            val title = "Message_from".localized + " " + status?.by?.getString()
            val message = status?.note
            context?.showAlert(title, message, "Close".localized)
        }
    }

    override fun onButtonClick(cell: RecyclerButtonCell, id: Any?) {
        if (id == "cancel") {
            cancel()
        }
    }

    private fun cancel() {
        instanceDatabase.token?.also { token ->
            McsConfigReasonApi().run { success, patientCancel, _, _ ->
                if (success) {
                    val cancels = mutableListOf<TextModel>()
                    for (item in patientCancel!!) {
                        cancels.add(TextModel(item.name))
                    }
                    context?.showAlert("Cancellation_reason_title".localized, cancels, "Cancel".localized) { model ->
                        val note = model.getText()
                        context?.showAlert(null, "Cancel_appointment_message".localized, "No".localized, "Agree".localized) {
                            McsPatientCancelAppointmentApi(token, appointment.id!!, note).run { success, _, code ->
                                if (success) {
                                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent(appointmentDidUpdatedNotification))
                                    context?.showAlert("Successful".localized, null, "Close".localized) {
                                        popBack()
                                    }
                                } else {
                                    // Error description
                                    // 403 Invalid/Expired token
                                    // 404 Not found
                                    // 406 Cannot be cancelled
                                    when {
                                        code == 404 -> {
                                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent(appointmentDidUpdatedNotification))
                                            context?.showAlert("Error".localized, "Not_found_appointment_message".localized, "Close".localized) {
                                                popBack()
                                            }
                                        }
                                        code == 406 -> {
                                            refresh()
                                            context?.showAlert("Error".localized, "No_cancelable_appointment_message".localized, "Close".localized) {
                                                popBack()
                                            }
                                        }
                                        code != 403 -> {
                                            context?.generalErrorAlert()
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    context?.generalErrorAlert()
                }
            }
        }
    }

}
