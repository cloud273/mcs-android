package com.cloud273.doctor.fragment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.localization.lTitle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.doctor.appointment.McsDoctorAppointmentDetailApi
import com.cloud273.backend.api.doctor.appointment.McsDoctorListAppointmentApi
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsAptStatusType
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.fragment.appointment.MDAppointmentFragment
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.model.isActive
import com.cloud273.doctor.view.MDAppointmentCell
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.*
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCenterCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_history_list_appointment.*
import java.util.*

class MDHistoryListAppointmentFragment: McsFragment(), RecyclerCellInterface {

    private lateinit var adapter: RecyclerAdapter
    private val packageType = McsPackageType.classic
    private val appointments = mutableListOf<McsAppointment>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "History_appointment"
        adapter = RecyclerAdapter(MDRecyclerRender(), this)

        return inflater.inflate(R.layout.fragment_md_history_list_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    override fun refresh() {
        appointments.clear()
        loadAppointments {
            if (isResumed) {
                if (it) {
                    reloadView()
                }
            }
            endRefresh()
        }
    }

    private fun noAppointmentText(): String = "No_appointment_doctor_message".localized

    private fun loadAppointments(completion: (Boolean) -> Unit) {
        val token = MDDatabase.instance.token
        if (token != null) {
            val to = Date().add(day = 1)
            val from = Date().add(month = -12).firstDayOfThisMonth()
            McsDoctorListAppointmentApi(token, packageType, arrayListOf(McsAptStatusType.finished), from, to).run { success, appointments, _ ->
                if (success) {
                    this.appointments.addAll(appointments!!)
                }
                completion(success)
            }
        } else {
            completion(false)
        }
    }

    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()
        for (apt in appointments) {
            cells.add(RecyclerCellData(id = "appointment", data = apt, resource = MDAppointmentCell.cellId))
        }
        if (cells.isEmpty()) {
            cells.add(RecyclerCellData(data = noAppointmentText(), resource = RecyclerTextCenterCell.cellId))
            recyclerView.unDrawDividerAt(0, cells.size - 1)
        }
        adapter.setData(cells)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        if (id == "appointment") {
            val apt = data as McsAppointment
            McsDoctorAppointmentDetailApi(MDDatabase.instance.token!!, apt.id!!).run { success, appointment, code ->
                if (success) {
                    val bundle = Bundle()
                    bundle.putString(MDAppointmentFragment.appointmentKey, SupportCenter.instance.push(appointment!!))
                    navigate(R.id.mdHistoryAppointmentFragment, bundle)
                } else {
                    if (code == 404) {
                        refresh()
                        requireContext().showAlert(title = "Error".localized, message = "Not_found_appointment_message".localized, close = "Close".localized)
                    }
                }
            }
        }
    }
}