package com.cloud273.patient.fragment.booking

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.mcs.fragment.McsCertificateFragment
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.model.McsRecyclerRender
import com.cloud273.patient.view.MPCertificateInfoCell
import com.cloud273.patient.view.MPAddressClinicCell
import com.cloud273.patient.view.MPDoctorInfoCell
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.recycler.common.unDrawDividerAt
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.view.RecyclerCircleImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.specialtiesString
import com.cloud273.mcs.util.titleHighlightContentCell
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.resourceColor
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.extension.yearOld
import kotlinx.android.synthetic.main.fragment_mp_booking_doctor.*

class MPBookingDoctorFragment : McsFragment(), RecyclerCellInterface, MPCertificateInfoCell.OnActionInterface {

    companion object {
        const val doctorKey = "doctor"
        const val appointmentKey = "appointment"
    }

    private val adapter = RecyclerAdapter(McsRecyclerRender(), this)
    private lateinit var doctor: McsDoctor
    private lateinit var appointment: MPAppointment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
            doctor = SupportCenter.instance.pop(it.getString(doctorKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Doctor".localized
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        continueBtn.setOnClickListener {
            next()
        }
        reloadView()
    }

    @SuppressLint("SetTextI18n")
    private fun reloadView() {
        if (doctor.packages.isNullOrEmpty()) {
            continueBtn.isEnabled = false
            continueBtn.setBackgroundColor(resourceColor(R.color.light_gray_color))
            continueBtn.text = "Choose".localized
        } else {
            continueBtn.isEnabled = true
            continueBtn.setBackgroundColor(resourceColor(R.color.blue_color))
            val pkg = doctor.packages!![0]
            continueBtn.text = "${"Choose".localized} (${"Price".localized} ${pkg.price.getString()})"
        }
        val cells = mutableListOf<RecyclerCellData>()
        cells.add(RecyclerCellData(data = ImageModel(doctor.image, R.mipmap.doctor_icon), resource = RecyclerCircleImageCell.cellId))
        cells.add(RecyclerCellData(data = doctor, resource = MPDoctorInfoCell.cellId))
        cells.add(RecyclerCellData(data = doctor.clinic!!, resource = MPAddressClinicCell.titleHighlightCellId))
        doctor.office?.also { office ->
            cells.add(RecyclerCellData(data = TextDetailModel("Office".localized, office), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        }
        cells.add(RecyclerCellData(data = TextDetailModel("Specialty".localized, doctor.specialtiesString(null)), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        doctor.startWork?.also { startWork ->
            val text = "${startWork.yearOld()} ${"years".localized}"
            cells.add(RecyclerCellData(data = TextDetailModel("Experience".localized, text), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        }
        doctor.biography?.also { biography ->
            cells.add(RecyclerCellData(data = TextDetailModel("Biography".localized, biography), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        }
        doctor.clinic?.certificates?.also { certs ->
            for (cert in certs) {
                cells.add(RecyclerCellData("certificate", cert, MPCertificateInfoCell.cellId))
            }
        }
        doctor.certificates?.also {
            for (cert in it) {
                cells.add(RecyclerCellData("certificate", cert, MPCertificateInfoCell.cellId))
            }
        }
        recyclerView.unDrawDividerAt(0, cells.size - 1)
        adapter.setData(cells)
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        super.onDrawCell(cell, id, data)
        if (id == "certificate") {
            (cell as MPCertificateInfoCell).listener = this
        }
    }

    override fun onInfoClick(cell: MPCertificateInfoCell, id: Any?, data: McsCertificate) {
        val bundle = Bundle()
        bundle.putString(McsCertificateFragment.certificateKey, SupportCenter.instance.push(data))
        navigate(R.id.certificateFragment, bundle)
    }

    private fun next() {
        doctor.packages?.first()?.also { pkg ->
            val helper = MPBookingTimeFragment.MPBookingTimeHelper(pkg.id!!)
            helper.refresh { code ->
                if (isResumed) {
                    if (code == 200) {
                        if (helper.isValid()) {
                            val bundle = Bundle()
                            appointment.set(pkg)
                            bundle.putString(MPBookingTimeFragment.appointmentKey, SupportCenter.instance.push(appointment))
                            bundle.putString(MPBookingTimeFragment.doctorKey, SupportCenter.instance.push(doctor))
                            bundle.putString(MPBookingTimeFragment.helperKey, SupportCenter.instance.push(helper))
                            navigate(R.id.bookingTimeFragment, bundle)
                        } else {
                            context?.showAlert(null, "Unavailable_doctor_message".localized, "Close".localized) {
                                popBack()
                            }
                        }
                    } else if (code == 404) {
                        context?.showAlert(null, "Unavailable_doctor_message".localized, "Close".localized) {
                            popBack()
                        }
                    } else if (code != 403) {
                        context?.generalErrorAlert()
                    }
                }
            }
        }
    }

}