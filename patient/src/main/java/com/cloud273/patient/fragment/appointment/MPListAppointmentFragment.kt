package com.cloud273.patient.fragment.appointment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.appointment.McsPatientAppointmentDetailApi
import com.cloud273.backend.api.patient.appointment.McsPatientListAppointmentApi
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.model.McsRecyclerRender
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.cloud273.backend.model.common.McsAppointment
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.center.moveToBookingNotification
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.util.refreshCell
import com.cloud273.patient.center.MPApp
import com.cloud273.patient.model.isActive
import com.cloud273.patient.view.MPAppointmentCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.add
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_list_appointment.*
import java.util.*

class MPListAppointmentFragment : McsFragment(), RecyclerCellInterface {

    private var shouldReload = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isResumed) {
                refresh()
            } else {
                shouldReload = true
            }
        }
    }

    private val adapter = RecyclerSectionAdapter(McsRecyclerRender(), this)
    private val packageType = McsPackageType.classic
    private var appointments: List<McsAppointment>? = null

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(appointmentDidUpdatedNotification))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_list_appointment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Booked_history"
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun onResume() {
        super.onResume()
        if (shouldReload) {
            refresh()
        }
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun refresh() {
        instanceDatabase.token?.also { token ->
            val to = Date().add(month = 3)
            val from = Date().add(month = -12)
            McsPatientListAppointmentApi(token, packageType, null, from, to).run { success, appointments, _ ->
                if (success) {
                    this.appointments = appointments
                    if (isResumed) {
                        reloadView()
                    }

                }
                endRefresh()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()
        val historyCells = mutableListOf<RecyclerCellData>()
        appointments?.also { list ->
            for (appointment in list) {
                if (appointment.isActive) {
                    cells.add(RecyclerCellData(data = appointment, resource = MPAppointmentCell.cellId))
                } else {
                    historyCells.add(RecyclerCellData(data = appointment, resource = MPAppointmentCell.cellId))
                }
            }
        }
        val sections = mutableListOf<RecyclerSectionData>().apply {
            if (cells.size > 0) {
                add(RecyclerSectionData(RecyclerHeaderFooterData("Waiting_examination".localized.uppercase()), cells, null))
            }
            if (historyCells.size > 0) {
                add(RecyclerSectionData(RecyclerHeaderFooterData("History_appointment".localized.uppercase()), historyCells, null))
            }
            if (cells.size == 0 && historyCells.size == 0) {
                add(RecyclerSectionData(cells = listOf(RecyclerCellData("empty", "No_appointment_patient_message".localized, RecyclerTextCell.refreshCell))))
                recyclerView.unDrawDividerAt(0)
            } else {
                recyclerView.drawAllDivider()
            }
        }
        adapter.setData(sections)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        if (id == "empty") {
            LocalBroadcastManager.getInstance(MPApp.instance).sendBroadcast(Intent(moveToBookingNotification))
        } else {
            instanceDatabase.token?.also { token ->
                val apt = data as McsAppointment
                McsPatientAppointmentDetailApi(token, apt.id!!).run { success, appointment, code ->
                    if (success) {
                        val bundle = Bundle()
                        bundle.putString(MPAppointmentFragment.appointmentKey, SupportCenter.instance.push(appointment!!))
                        navigate(R.id.appointmentFragment, bundle)
                    } else {
                        // Error description
                        // 403 Invalid/Expired token
                        // 404 Not found
                        if (code == 404) {
                            refresh()
                            context?.showAlert("Error".localized, "Not_found_appointment_message".localized, "Close".localized)
                        } else if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

}
