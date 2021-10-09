package com.cloud273.patient.fragment.health

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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.health.McsPatientHealthDetailApi
import com.cloud273.backend.api.patient.health.medication.McsPatientMedicationUpdateApi
import com.cloud273.mcs.center.accountHealthInfoDidChangeNotification
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.util.toAppDateString
import com.cloud273.patient.R
import com.cloud273.patient.center.MPDatabase
import com.cloud273.patient.model.McsRecyclerRender
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.model.TextDetailInterface
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerCheckCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.common.McsTrilean
import com.cloud273.backend.model.patient.McsAllergy
import com.cloud273.backend.model.patient.McsMedication
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.backend.model.patient.McsSurgery
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.medications
import com.cloud273.mcs.util.addTextCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import kotlinx.android.synthetic.main.fragment_mp_health_profile.*
import java.util.*

open class MPHealthProfileFragment: McsFragment(), RecyclerCellInterface, RecyclerCheckCell.OnActionInterface {

    class Allergy(private val allergy: McsAllergy): TextDetailInterface {
        override fun getText(): String {
            return allergy.name
        }

        override fun getDetailText(): String? {
            return allergy.note
        }
    }

    class Surgery(private val surgery: McsSurgery): TextDetailInterface {

        override fun getText(): String {
            return surgery.name + " (" + surgery.date.toAppDateString() + ")"
        }

        override fun getDetailText(): String? {
            return surgery.note
        }

    }


    private lateinit var adapter: RecyclerSectionAdapter

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isResumed) {
                reloadView()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(accountHealthInfoDidChangeNotification))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_health_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Health_information"
        adapter = RecyclerSectionAdapter(McsRecyclerRender(), this)
        recyclerView().listLayout()
        recyclerView().adapter = adapter
        reloadView()
    }

    open fun recyclerView(): RecyclerView {
        return recyclerView
    }

    open fun navigationAllergyId(): Int {
        return R.id.allergyFragment
    }

    open fun navigationSurgeryId(): Int {
        return R.id.surgeryFragment
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    @SuppressLint("DefaultLocale")
    open fun sessions(): MutableList<RecyclerSectionData> {
        return mutableListOf<RecyclerSectionData>().apply {
            instanceDatabase.account?.also { account ->
                val patient = account as McsPatient
                val allergyCells = mutableListOf<RecyclerCellData>()
                val surgeryCells = mutableListOf<RecyclerCellData>()
                val medicationCells = mutableListOf<RecyclerCellData>()
                patient.allergies?.also {
                    for (allergy in it) {
                        allergyCells.add(RecyclerCellData(allergy, Allergy(allergy), RecyclerTextDetailCell.cellId))
                    }
                }
                allergyCells.add(RecyclerCellData("allergy", "Add_allergy".localized, RecyclerTextCell.addTextCell))
                patient.surgeries?.also {
                    for (surgery in it) {
                        surgeryCells.add(RecyclerCellData(surgery, Surgery(surgery), RecyclerTextDetailCell.cellId))
                    }
                }
                surgeryCells.add(RecyclerCellData("surgery", "Add_surgery".localized, RecyclerTextCell.addTextCell))
                patient.medications?.also {
                    for (medication in it) {
                        medicationCells.add(RecyclerCellData(medication, RecyclerCheckCell.Model(medication.name!!.getString(), medication.value == McsTrilean.yes), RecyclerCheckCell.cellId))
                    }
                }
                add(RecyclerSectionData(RecyclerHeaderFooterData("Allergy".localized.uppercase()), allergyCells, null))
                add(RecyclerSectionData(RecyclerHeaderFooterData("Surgery".localized.uppercase()), surgeryCells, null))
                add(RecyclerSectionData(RecyclerHeaderFooterData("Medication".localized.uppercase()), medicationCells, null))
            }

        }
    }

    override fun refresh() {
        instanceDatabase.token?.also { token ->
            McsPatientHealthDetailApi(token).run { success, allergies, surgeries, medications, _ ->
                if (success) {
                    MPDatabase.instance.updateAccount(allergies!!, surgeries!!, medications!!)
                }
                endRefresh()
            }
        }
    }

    protected fun reloadView() {
        adapter.setData(sessions())
    }

    override fun onSwitchChange(cell: RecyclerCheckCell, id: Any?, value: Boolean) {
        instanceDatabase.token?.also { token ->
            val cMedication: McsMedication = id as McsMedication
            val medication = McsMedication.update(cMedication.id!!, if (value) McsTrilean.yes else McsTrilean.no, cMedication.note)
            McsPatientMedicationUpdateApi(token, medication).run { success, _, code ->
                if (success) {
                    medication.name = cMedication.name
                    MPDatabase.instance.update(medication)
                } else if (code != 403) {
                    reloadView()
                    context?.generalErrorAlert()
                }
            }
        }
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        super.onDrawCell(cell, id, data)
        if (cell is RecyclerCheckCell) {
            cell.listener = this
        }
    }

    private fun openAllergy(allergy: McsAllergy?) {
        var bundle: Bundle? = null
        if (allergy != null) {
            bundle = Bundle()
            bundle.putString(MPAllergyFragment.allergyKey, SupportCenter.instance.push(allergy))
        }
        navigate(navigationAllergyId(), bundle)
    }

    private fun openSurgery(surgery: McsSurgery?) {
        var bundle: Bundle? = null
        if (surgery != null) {
            bundle = Bundle()
            bundle.putString(MPSurgeryFragment.surgeryKey, SupportCenter.instance.push(surgery))
        }
        navigate(navigationSurgeryId(), bundle)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        when (id) {
            "allergy" -> {
                openAllergy(null)
            }
            "surgery" -> {
                openSurgery(null)
            }
            is McsAllergy -> {
                openAllergy(id)
            }
            is McsSurgery -> {
                openSurgery(id)
            }
        }
    }

}