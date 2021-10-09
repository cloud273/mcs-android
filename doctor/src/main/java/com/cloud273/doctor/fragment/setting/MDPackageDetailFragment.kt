package com.cloud273.doctor.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.doctor.`package`.McsDoctorPackageDetailApi
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDTimeModel
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.durationString
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.name
import com.cloud273.mcs.model.specialty
import com.cloud273.mcs.util.titleContentCell
import com.cloud273.mcs.util.toAppDateString
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_package_detail.*

class MDPackageDetailFragment: McsFragment(), RecyclerCellInterface {
    companion object {
        const val packageKey = "_package"
    }

    private lateinit var adapter: RecyclerSectionAdapter
    private var mcsPackage: McsPackage? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            mcsPackage = SupportCenter.instance.pop(getString(packageKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Detail"

        adapter = RecyclerSectionAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_package_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    override fun refresh() {
        if (mcsPackage != null) {
            val token = MDDatabase.instance.token
            if (token != null) {
                McsDoctorPackageDetailApi(token, mcsPackage!!.id!!).run { success, pack, code ->
                    if (success) {
                        mcsPackage = pack
                        reloadView()
                    } else if (code == 404) {
                        requireContext().showAlert(title = "Error".localized, message = "Not_found".localized, close = "Close".localized) {
                            popBack()
                        }
                    }
                    endRefresh()
                }
            } else {
                endRefresh()
            }
        } else {
            endRefresh()
        }
    }

    private fun reloadView() {
        mcsPackage?.also {
            val sections = mutableListOf<RecyclerSectionData>()
            val infoCells = mutableListOf<RecyclerCellData>()

            infoCells.add(RecyclerCellData(data = TextDetailModel("Appointment_type".localized, it.type.getString()), resource = RecyclerTextDetailCell.titleContentCell))
            infoCells.add(RecyclerCellData(data = TextDetailModel("Price".localized, it.price.getString()), resource = RecyclerTextDetailCell.titleContentCell))
            infoCells.add(RecyclerCellData(data = TextDetailModel("Appointment_duration".localized, it.durationString), resource = RecyclerTextDetailCell.titleContentCell))
            infoCells.add(RecyclerCellData(data = TextDetailModel("Specialty".localized, it.specialty.name), resource = RecyclerTextDetailCell.titleContentCell))
            sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Basic_information".localized), infoCells, null))

            it.schedules?.also { schedules ->
                for (schedule in schedules) {
                    val scheduleCells = mutableListOf<RecyclerCellData>()
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Monday".localized, schedule.monday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Tuesday".localized, schedule.tuesday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Wednesday".localized, schedule.wednesday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Thursday".localized, schedule.thursday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Friday".localized, schedule.friday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Saturday".localized, schedule.saturday), resource = RecyclerTextDetailCell.titleContentCell))
                    scheduleCells.add(RecyclerCellData(data = MDTimeModel("Sunday".localized, schedule.sunday), resource = RecyclerTextDetailCell.titleContentCell))
                    val timeString = "${"Schedule".localized} (${"From".localized} ${schedule.duration.from.toAppDateString()} ${"To".localized} ${schedule.duration.to.toAppDateString()})"
                    sections.add(RecyclerSectionData(RecyclerHeaderFooterData(timeString), scheduleCells, null))
                }
            }

            adapter.setData(sections)
        }
    }
}