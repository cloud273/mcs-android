package com.cloud273.patient.fragment.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.patient.booking.McsPatientListBookingTimeApi
import com.cloud273.backend.api.patient.booking.McsPatientListWorkingTimeApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.center.MPDatabase
import com.cloud273.patient.model.McsRecyclerRender
import com.dungnguyen.qdcore.extension.*
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.dungnguyen.qdcore.recycler.common.RecyclerCellData
import com.dungnguyen.qdcore.recycler.common.RecyclerCellInterface
import com.dungnguyen.qdcore.recycler.adapter.RecyclerGridSectionAdapter
import com.cloud273.mcs.view.McsBorderTextCell
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.backend.model.doctor.McsWorkingTime
import com.cloud273.backend.util.toDateApiDateTimeFormat
import com.cloud273.mcs.center.McsAppConfiguration
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.util.toAppTimeString
import com.cloud273.patient.model.MPAppointment
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.kalendar.KalendarView
import com.dungnguyen.qdcore.model.TextInterface
import com.dungnguyen.qdcore.recycler.common.gridVerticalLayout
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_booking_time.*
import java.lang.RuntimeException
import java.util.*

class MPBookingTimeFragment : McsFragment(), RecyclerCellInterface {

    class MPBookingTimeHelper(private var packageId: Int) {

        class Day(val workings: MutableList<McsWorkingTime>, val date: Date): KalendarView.Event {
            override fun eventDate(): Date {
                return date
            }
        }

        class Time(val working: McsWorkingTime, var time: Date): TextInterface {
            override fun getText(): String {
                return time.toAppTimeString()
            }
        }

        var selectDay: Day? = null
        var selectTime: Time? = null
        var days: List<Day>? = null
        var times: List<List<Time>>? = null
        var min: Date? = null
        var max: Date? = null

        fun isValid() : Boolean {
            return selectDay != null && selectTime != null
        }

        private fun getFrom() : Date =
            Date().add(second = -McsAppConfiguration.instance.creatableEnd)

        fun refresh(completion: (code: Int) -> Unit) {
            val from = getFrom()
            val to = from.add(month = 1)
            val token = MPDatabase.instance.token
            if (token != null) {
                McsPatientListWorkingTimeApi(token, packageId, from, to).run { success, workingTimes, code  ->
                    if (success) {
                        val list = mutableListOf<Day>()
                        if (workingTimes != null) {
                            for (wt in workingTimes) {
                                var found = false
                                for (item in list) {
                                    if (wt.begin.isSameDayAs(item.date)) {
                                        item.workings.add(wt)
                                        found = true
                                        break
                                    }
                                }
                                if (!found) {
                                    list.add(Day(mutableListOf(wt), wt.begin))
                                }
                            }
                        }
                        this.days = list
                        this.processDay()
                        this.reloadTime(completion)
                    } else {
                        completion(code)
                    }
                }
            } else {
                completion(403)
            }
        }

        private fun processDay() {
            var min: Date? = null
            var max: Date? = null
            var selectDay: Day? = null
            days?.also {days ->
                for (day in days) {
                    val date = day.date
                    if (min == null || min!! > date) {
                        min = date
                    }
                    if (max == null || max!! < date) {
                        max = date
                    }
                }
                val sel = selectDay
                if (sel != null) {
                    for (day in days) {
                        if (day.date.isSameDayAs(sel.date)) {
                            selectDay = day
                            break
                        }
                    }
                }
                if (selectDay == null) {
                    selectDay = days.firstOrNull()
                }
            }
            this.min = min
            this.max = max
            this.selectDay = selectDay
        }

        private fun loadBookingTime(token: String, from: Date, workings: List<McsWorkingTime>, index: Int, input: List<List<Time>>, completion: (bookingTimes: List<List<Time>>?, code: Int) -> Unit) {
            if (index < 0 || index >= workings.size) {
                throw RuntimeException("Wrong Input")
            } else {
                val wt = workings[index]
                McsPatientListBookingTimeApi(token, packageId, wt.begin, wt.end).run { success, bookingTimes, code  ->
                    if (success) {
                        if (!bookingTimes.isNullOrEmpty()) {
                            val tmp = mutableListOf<Time>()
                            for (time in bookingTimes) {
                                time.toDateApiDateTimeFormat()?.also { date ->
                                    if (date >= from) {
                                        tmp.add(Time(wt, date))
                                    }
                                }
                            }
                            val list = input.toMutableList()
                            list.add(tmp)
                            if (index == workings.size - 1) {
                                completion(list, code)
                            } else {
                                loadBookingTime(token, from, workings, index + 1, list, completion)
                            }
                        }
                    } else {
                        completion(input, code)
                    }
                }
            }
        }

        fun reloadTime(completion: (code: Int) -> Unit) {
            val token = MPDatabase.instance.token
            if (token != null) {
                val selectDay = this.selectDay
                if (selectDay != null && selectDay.workings.size > 0) {
                    val from = getFrom()
                    loadBookingTime(token, from, selectDay.workings, 0, listOf()) { list, code ->
                        this.times = list?.sortedBy {
                            it.first().time
                        }
                        val sel = this.selectTime
                        if (sel != null) {
                            this.selectTime = null
                            if (!list.isNullOrEmpty()) {
                                for (obs in list) {
                                    for (obj in obs) {
                                        if (obj.time == sel.time) {
                                            this.selectTime = obj
                                            break
                                        }
                                    }
                                    if (this.selectTime != null) {
                                        break
                                    }
                                }
                            }
                        }
                        if (this.selectTime == null) {
                            this.selectTime = this.times?.firstOrNull()?.firstOrNull()
                        }
                        completion(code)
                    }
                } else {
                    times = null
                    selectTime = null
                    completion(200)
                }
            } else {
                completion(403)
            }
        }
    }

    companion object {
        const val doctorKey = "doctor"
        const val appointmentKey = "appointment"
        const val helperKey = "helper"
    }

    private val adapter = RecyclerGridSectionAdapter(McsRecyclerRender(), this)

    private lateinit var doctor: McsDoctor
    private lateinit var appointment: MPAppointment
    private lateinit var helper: MPBookingTimeHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            appointment = SupportCenter.instance.pop(it.getString(appointmentKey)!!)
            doctor = SupportCenter.instance.pop(it.getString(doctorKey)!!)
            helper = SupportCenter.instance.pop(it.getString(helperKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_booking_time, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Choose_time"
        recyclerView.gridVerticalLayout(3, resources.getDimension(R.dimen.offset).toInt())
        recyclerView.adapter = adapter
        reloadCalendar()
        reloadNextButton()
        nextBtn.setOnClickListener {
            next()
        }
    }

    private fun reloadNextButton() {
        if (helper.selectTime == null) {
            nextBtn.isEnabled = false
            nextBtn.setBackgroundColor(resourceColor(R.color.light_gray_color))
        } else {
            nextBtn.isEnabled = true
            nextBtn.setBackgroundColor(resourceColor(R.color.blue_color))
        }
    }

    private fun reloadRecyclerTimeView() {
        val sections = mutableListOf<List<RecyclerCellData>>()
        for (times in helper.times!!) {
            val section = mutableListOf<RecyclerCellData>()
            for (time in times) {
                section.add(RecyclerCellData(data = time, resource = McsBorderTextCell.cellId))
            }
            sections.add(section)
        }
        adapter.setData(sections)
    }

    private fun reloadCalendar() {
        var minDate: Date = helper.min!!
        var maxDate: Date = helper.max!!
        for (day in helper.days!!) {
            val date = day.date
            if (minDate > date) {
                minDate = date
            }
            if (maxDate < date) {
                maxDate = date
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

        calendarView.setup(Locale(instanceDatabase.language.value), minDate, maxDate, helper.selectDay!!.date, true, helper.days) { date ->
            var selDay: MPBookingTimeHelper.Day? = null
            for (day in helper.days!!) {
                if (date.isSameDayAs(day.date)) {
                    selDay = day
                    break
                }
            }
            if (selDay != null && selDay != helper.selectDay) {
                helper.selectDay = selDay
                helper.reloadTime {
                    if (isResumed) {
                        reloadRecyclerTimeView()
                    }
                }
            } else  {
                println(date)
            }
        }
        reloadRecyclerTimeView()
    }

    private fun next() {
        appointment.set(helper.selectTime!!.time)
        val bundle = Bundle()
        bundle.putString(MPBookingSummaryFragment.appointmentKey, SupportCenter.instance.push(appointment))
        bundle.putString(MPBookingSummaryFragment.doctorKey, SupportCenter.instance.push(doctor))
        navigate(R.id.bookingSummaryFragment, bundle)
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        val wt = data as MPBookingTimeHelper.Time
        val bCell = cell as McsBorderTextCell
        bCell.additionSetup(wt == helper.selectTime)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        helper.selectTime = data as MPBookingTimeHelper.Time
        adapter.notifyDataSetChanged()
        reloadNextButton()
    }

}
