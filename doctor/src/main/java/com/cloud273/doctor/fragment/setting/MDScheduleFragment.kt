package com.cloud273.doctor.fragment.setting

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
import com.cloud273.backend.api.doctor.`package`.McsDoctorListPackageApi
import com.cloud273.backend.api.doctor.working.McsDoctorListWorkingApi
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.backend.model.doctor.McsWorkingTime
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDWorkingTimeModel
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.mcs.center.appointmentDidUpdatedNotification
import com.dungnguyen.qdcore.kalendar.KalendarView
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.center.scheduleDidUpdatedNotification
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.util.blueTextCell
import com.cloud273.mcs.util.toAppDateString
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.*
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_schedule.*
import java.util.*

class MDScheduleFragment: McsFragment(), RecyclerCellInterface {
    companion object {
        const val workingTimesKey = "_working_time"
        const val packagesKey = "_packages"
        const val fromKey = "_from"
        const val toKey = "_to"
    }

    private var shouldReload = false
    private lateinit var adapter: RecyclerAdapter
    private var workingTimes: List<MDWorkingTime>? = null
    private var packages: List<McsPackage>? = null
    private var from: Date? = null
    private var to: Date? = null

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isResumed) {
                refresh()
            } else {
                shouldReload = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(
            scheduleDidUpdatedNotification
        )
        )
        arguments?.apply {
            workingTimes = SupportCenter.instance.pop(getString(workingTimesKey)!!)
            packages = SupportCenter.instance.pop(getString(packagesKey)!!)
            from = SupportCenter.instance.pop(getString(fromKey)!!)
            to = SupportCenter.instance.pop(getString(toKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Schedule"

        adapter = RecyclerAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        setupCalendar()
        reloadView()
    }

    override fun onResume() {
        super.onResume()
        if (shouldReload) {
            refresh()
        }
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    private fun setupCalendar() {
        val min = from!!
        val max = to!!
        val today = Date()
        val selectedDate = when {
            today < min -> {
                min
            }
            today > max -> {
                max
            }
            else -> {
                today
            }
        }
        calendarView.initialize(
            resourceColor(R.color.blue_color),
            resourceColor(R.color.white_color),
            resourceColor(R.color.red_color),
            resourceColor(R.color.white_color),
            resourceColor(R.color.blue_color),
            resourceColor(R.color.gray_color),
            resourceColor(R.color.black_color),
            resourceColor(R.color.blue_color)
        )

        calendarView.setup(Locale(instanceDatabase.language.value), min, max, selectedDate, false, workingTimes) {
            reloadView()
        }
    }

    override fun refresh() {
        MDWorkingTimeHelper.reload { success, from, to, workingTime, packages, _ ->
            if (success) {
                this.workingTimes = workingTime
                this.packages = packages
                this.from = from
                this.to = to
                setupCalendar()
                reloadView()
            }
            endRefresh()
        }
    }

    private fun reloadView() {
        workingTimes?.also {
            val cells = mutableListOf<RecyclerCellData>()
            val selectedDate = calendarView.selectedDate

            for (wt in it) {
                if (wt.begin.isSameDayAs(selectedDate)) {
                    cells.add(RecyclerCellData(id = "schedule", data = MDWorkingTimeModel(wt), resource = RecyclerTextCell.cellId))
                }
            }

            if (cells.isNotEmpty()) {
                val text = "${"Schedule_on".localized} ${selectedDate.toAppDateString()}"
                cells.add(0, RecyclerCellData(id = "schedule", data = text, resource = RecyclerTextCell.blueTextCell))
            } else {
                val text = "${"No_schedule".localized} ${selectedDate.toAppDateString()}"
                cells.add(RecyclerCellData(id = "schedule", data = text, resource = RecyclerTextCell.blueTextCell))
            }

            recyclerView.unDrawDivider()
            adapter.setData(cells)
        }
    }

}

object MDWorkingTimeHelper {
    fun reload(completion: (success: Boolean, from: Date, to: Date , workingTime: List<MDWorkingTime>?, packages: List<McsPackage>?, expired: Boolean) -> Unit) {
        val from = Date().beginDate()
        val to = Date().add(month = 2).lastDayOfThisMonth()
        val token = MDDatabase.instance.token!!
        McsDoctorListPackageApi(token).run { success, packages, code ->
            when {
                success -> {
                    McsDoctorListWorkingApi(token, from, to).run { wSuccess, workingTimes, wCode ->
                        when {
                            wSuccess -> {
                                val list = arrayListOf<MDWorkingTime>()
                                for (wt in workingTimes!!) {
                                    try {
                                        val p = packages!!.first { pk -> pk.id == wt.packageId}
                                        list.add(MDWorkingTime(
                                            p, packages.size > 1, wt.begin, wt.end, wt.visitTime, wt.packageId, wt.scheduleId, wt.workingDayId
                                        ))
                                    } catch (ex: NoSuchElementException) {}
                                }
                                completion(true, from, to, list, packages, false)
                            }
                            wCode == 403 -> {
                                completion(false, from, to, listOf(), packages, true)
                            }
                            else -> {
                                completion(false, from, to, listOf(), packages, false)
                            }
                        }
                    }
                }
                code == 403 -> {
                    completion(false, from, to, listOf(), packages, true)
                }
                else -> {
                    completion(false, from, to, listOf(), packages, false)
                }
            }
        }
    }
}

class MDWorkingTime(val pack: McsPackage, val isMultiplePackage: Boolean, begin: Date, end: Date, visitTime: Int, packageId: Int?, scheduleId: Int?, workingDayId: Int?): McsWorkingTime(begin, end, visitTime, packageId, scheduleId, workingDayId), KalendarView.Event {
    override fun eventDate(): Date {
        return begin
    }
}