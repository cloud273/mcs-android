package com.cloud273.patient.fragment.booking

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.model.common.McsPackageType
import com.cloud273.mcs.fragment.McsFragment

import com.cloud273.patient.R
import com.cloud273.patient.activity.MPBookingActivity
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_booking_package_type.*

class MPBookingPackageTypeFragment : McsFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_package_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Booking"
        classicButton.setOnClickListener {
            val intent = Intent(requireContext(), MPBookingActivity::class.java)
            intent.putExtra(MPBookingActivity.appointmentKey, SupportCenter.instance.push(MPAppointment.create(McsPackageType.classic)))
            startActivity(intent)
        }
        telemedButton.setOnClickListener {
            context?.showAlert("Coming_soon".localized, null, "Close".localized)
        }
    }

}
