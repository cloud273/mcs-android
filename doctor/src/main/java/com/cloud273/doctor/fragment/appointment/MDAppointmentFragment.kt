package com.cloud273.doctor.fragment.appointment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.doctor.appointment.McsDoctorAppointmentDetailApi
import com.cloud273.backend.api.doctor.appointment.McsDoctorBeginAppointmentApi
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsAptStatusType
import com.cloud273.backend.model.common.McsUserType
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.view.MDOptionCell
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.*
import com.cloud273.mcs.util.*
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerButtonCell
import com.dungnguyen.qdcore.recycler.view.RecyclerCircleImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_appointment.*

open class MDAppointmentFragment: McsFragment(), RecyclerCellInterface,
    RecyclerButtonCell.OnActionInterface {
    companion object {
        const val appointmentKey = "appointment"
    }

    private lateinit var appointment: McsAppointment
    private lateinit var adapter: RecyclerSectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Detail"
        adapter = RecyclerSectionAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    override fun refresh() {
        val token = MDDatabase.instance.token
        if (token != null) {
            McsDoctorAppointmentDetailApi(token, appointment.id!!).run { success, appointment, _ ->
                if (success) {
                    this.appointment = appointment!!
                    reloadView()
                }
                endRefresh()
            }
        } else {
            endRefresh()
        }
    }

    private fun reloadView() {
        val sections = mutableListOf<RecyclerSectionData>()
        val imageCell = mutableListOf<RecyclerCellData>()
        val bookingCells = mutableListOf<RecyclerCellData>()
        val healthCells = mutableListOf<RecyclerCellData>()

        imageCell.add(RecyclerCellData(data = ImageModel(appointment.patientInfo?.image, R.mipmap.profile_icon), resource = RecyclerCircleImageCell.cellId))
        sections.add(RecyclerSectionData(null, imageCell, null))

        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Status".localized, detail = appointment.status!!.value.getString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Order".localized, detail = appointment.order!!.toString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Patient".localized, detail = appointment.patientInfo!!.profile!!.fullName), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Specialty".localized, detail = appointment.specialty.name), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Time".localized, detail = appointment.begin.toAppDateTimeString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        bookingCells.add(RecyclerCellData(data = TextDetailModel(title = "Price".localized, detail = appointment.price.getString()), resource = RecyclerTextDetailCell.titleContentCell))
        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Booking_information".localized), bookingCells, null))

        var text = ""
        for (symptom in appointment.symptoms) {
            if (text.isNotEmpty()) {
                text += "\n"
            }
            text += "+ ${symptom.name}: ${symptom.note}"
        }
        healthCells.add(RecyclerCellData(data = TextDetailModel("Symptom".localized, text), resource = RecyclerTextDetailCell.titleContentCell))

        if (!appointment.allergies.isNullOrEmpty()) {
            text = ""
            for (allergy in appointment.allergies!!) {
                if (text.isNotEmpty()) {
                    text += "\n"
                }
                text += "+ ${allergy.name}: ${allergy.note}"
            }
            healthCells.add(RecyclerCellData(data = TextDetailModel("Allergy".localized, text), resource = RecyclerTextDetailCell.titleContentCell))
        }
        if (!appointment.surgeries.isNullOrEmpty()) {
            text = ""
            for (surgery in appointment.surgeries!!) {
                if (text.isNotEmpty()) {
                    text += "\n"
                }
                text += "+ ${surgery.name}: ${surgery.note}"
            }
            healthCells.add(RecyclerCellData(data = TextDetailModel("Surgery".localized, text), resource = RecyclerTextDetailCell.titleContentCell))
        }
        if (!appointment.medications.isNullOrEmpty()) {
            text = ""
            for (medication in appointment.medications!!) {
                if (text.isNotEmpty()) {
                    text += "\n"
                }
                text += "+ ${medication.name?.getString()}: ${medication.value.getString()}"
            }
            healthCells.add(RecyclerCellData(data = TextDetailModel("Medication".localized, text), resource = RecyclerTextDetailCell.titleContentCell))
        }
        if (isShowButton()) {
            if (appointment.beginable!!) {
                healthCells.add(RecyclerCellData(id = "begin", data = "Begin".localized, resource = RecyclerButtonCell.greenButtonCell))
            } else {
                healthCells.add(RecyclerCellData(id = "begin", data = "Begin".localized, resource = RecyclerButtonCell.grayButtonCell))
            }
        }
        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Health_information".localized), healthCells, null))

        adapter.setData(sections)
    }

    override fun onButtonClick(cell: RecyclerButtonCell, id: Any?) {
        if (id == "begin") {
            if (appointment.beginable!!) {
                requireContext().showAlert(title = null, message = "Begin_appointment_message".localized, no = "No".localized, yes = "Agree".localized) {
                    // Error description
                    // 403 Invalid/Expired token
                    // 404 Not found
                    // 406 Cannot started
                    val note = "Started";
                    McsDoctorBeginAppointmentApi(MDDatabase.instance.token!!, appointment.id!!, note).run { success, _, code ->
                        if (success) {
                            appointment.status?.value = McsAptStatusType.started
                            appointment.status?.by = McsUserType.doctor
                            appointment.status?.note = note
                            reloadView()
                            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                Intent(appointmentDidUpdatedNotification)
                            )
//                                navigate(R.id.mdPrescriptionFragment)
                        } else {
                            if (code == 404) {
                                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                    Intent(appointmentDidUpdatedNotification)
                                )
                                requireContext().showAlert(title = "Error".localized, message = "Not_found_appointment_message".localized, close = "Close".localized) {
                                    popBack()
                                }
                            } else if (code == 406) {
                                LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(
                                    Intent(appointmentDidUpdatedNotification)
                                )
                                refresh()
                                requireContext().showAlert(title = "Error".localized, message = "No_beginable_appointment_message".localized, close = "Close".localized)
                            }
                        }
                    }
                }
            } else {
                requireContext().showAlert(title = null, message = "Wait".localized, close = "Close".localized)
            }
        }
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        super.onDrawCell(cell, id, data)
        if (id == "begin") {
            val btnCell = (cell as RecyclerButtonCell)
            btnCell.listener = this
        }
    }

    protected open fun isShowButton(): Boolean = true
}