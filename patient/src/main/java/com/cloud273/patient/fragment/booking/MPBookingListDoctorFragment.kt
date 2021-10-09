package com.cloud273.patient.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.booking.McsPatientDoctorDetailApi
import com.cloud273.backend.api.patient.booking.McsPatientListDoctorApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.model.McsRecyclerRender
import com.cloud273.patient.view.MPDoctorCell
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.util.refreshCell
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import kotlinx.android.synthetic.main.fragment_mp_booking_list_doctor.*

class MPBookingListDoctorFragment : McsFragment(), RecyclerCellInterface {

    companion object {
        const val doctorsKey = "doctors"
        const val appointmentKey = "appointment"
    }
    private val adapter = RecyclerAdapter(McsRecyclerRender(), this)
    private lateinit var doctors: List<McsDoctor>
    private lateinit var appointment: MPAppointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            doctors = SupportCenter.instance.pop(it.getString(doctorsKey)!!)
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_list_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Doctor"
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun refresh() {
        instanceDatabase.token?.also { token ->
            McsPatientListDoctorApi(token, appointment.type!!, appointment.specialtyCode!!).run { success, doctors, _ ->
                if (isResumed) {
                    if (success) {
                        this.doctors = doctors!!
                        reloadView()
                    }
                    endRefresh()
                }
            }
        }
    }

    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()
        if (doctors.isEmpty()) {
            cells.add(RecyclerCellData(data = "No_doctor_available_and_refresh_message".localized, resource = RecyclerTextCell.refreshCell))
            recyclerView.unDrawDividerAt(0)
        } else{
            for (doctor in doctors) {
                cells.add(RecyclerCellData("doctor", MPDoctorCell.Data(appointment.specialty, doctor), MPDoctorCell.cellId))
            }
            recyclerView.drawAllDivider()
        }

        adapter.setData(cells)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        if (id == "doctor") {
            val obj = data as MPDoctorCell.Data
            val doctor = obj.doctor
            val specialty = obj.specialty
            instanceDatabase.token?.also { token ->
                McsPatientDoctorDetailApi(token, doctor.id!!, appointment.type!!, specialty.code).run { success, doctor, code ->
                    if (isResumed) {
                        if (success) {
                            val bundle = Bundle()
                            bundle.putString(
                                MPBookingDoctorFragment.appointmentKey,
                                SupportCenter.instance.push(appointment)
                            )
                            bundle.putString(
                                MPBookingDoctorFragment.doctorKey,
                                SupportCenter.instance.push(doctor!!)
                            )
                            navigate(R.id.bookingDoctorDetailFragment, bundle)
                        } else {
                            // Error description
                            // 403 Invalid/Expired token
                            // 404 Not found
                            if (code == 404) {
                                refresh()
                                context?.showAlert(
                                    "Error".localized,
                                    "Not_found".localized,
                                    "Close".localized
                                )
                            } else if (code != 403) {
                                context?.generalErrorAlert()
                            }
                        }
                    }
                }
            }
        }
    }

}