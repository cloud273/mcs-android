package com.cloud273.patient.fragment.booking

import android.os.Bundle
import android.view.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.booking.McsPatientListDoctorApi
import com.cloud273.backend.api.patient.booking.McsPatientListSpecialtyApi
import com.cloud273.backend.model.common.McsSpecialty
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment

import com.cloud273.patient.R
import com.cloud273.patient.model.MPAppointment
import com.cloud273.patient.model.McsRecyclerRender
import com.cloud273.patient.view.MPSpecialtyCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.gridVerticalLayout
import com.dungnguyen.qdcore.extension.screenSize
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.common.RecyclerCellData
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.dungnguyen.qdcore.recycler.common.RecyclerCellInterface
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_booking_specialty.*

class MPBookingSpecialtyFragment : McsFragment(), RecyclerCellInterface {

    private val adapter = RecyclerAdapter(McsRecyclerRender(), this)

    companion object {
        const val appointmentKey = "appointment"
        const val specialtiesKey = "specialties"
    }

    private lateinit var appointment: MPAppointment
    private lateinit var specialties: List<McsSpecialty>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
            specialties = SupportCenter.instance.pop(it.getString(specialtiesKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_specialty, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Specialty"
        recyclerView.gridVerticalLayout(2, requireContext().screenSize().width/8)
        recyclerView.adapter = adapter
        reloadView()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun refresh() {
        instanceDatabase.token?.also { token ->
            McsPatientListSpecialtyApi(token, appointment.symptoms!!).run { success, specialties, code ->
                if (isResumed) {
                    if (success) {
                        this.specialties = specialties!!
                        reloadView()
                    } else {
                        if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                    endRefresh()
                }

            }
        }
    }

    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()
        for (specialty in specialties) {
            cells.add(RecyclerCellData(data = specialty, resource = MPSpecialtyCell.cellId))
        }
        adapter.setData(cells)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        val specialtyCode = (data as McsSpecialty).code
        instanceDatabase.token?.also { token ->
            McsPatientListDoctorApi(token, appointment.type!!, specialtyCode).run { success, doctors, code ->
                if (isResumed) {
                    if (success) {
                        if (doctors!!.isEmpty()) {
                            refresh()
                            context?.showAlert(null, "No_doctor_available_message".localized, "Close".localized)
                        } else {
                            appointment.specialtyCode = specialtyCode
                            val bundle = Bundle()
                            bundle.putString(MPBookingListDoctorFragment.appointmentKey, SupportCenter.instance.push(appointment))
                            bundle.putString(MPBookingListDoctorFragment.doctorsKey, SupportCenter.instance.push(doctors))
                            navigate(R.id.bookingListDoctorFragment, bundle)
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
