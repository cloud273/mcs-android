package com.dungnguyen.qdcore.kalendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.extension.*
import java.text.DateFormatSymbols
import java.util.*

class KalendarView: LinearLayout, KalendarDayView.ListenerInterface {

    interface Event {

        fun eventDate(): Date

    }

    constructor(context: Context): super(context, null) { initData() }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet, 0) { initData() }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { initData() }

    private lateinit var header: LinearLayout
    private lateinit var titleTV: TextView
    private lateinit var firstTV: TextView
    private lateinit var secondTV: TextView
    private lateinit var thirdTV: TextView
    private lateinit var fourthTV: TextView
    private lateinit var fifthTV: TextView
    private lateinit var sixthTV: TextView
    private lateinit var seventhTV: TextView
    private lateinit var backBtn: ImageButton
    private lateinit var nextBtn: ImageButton

    private val days = Matrix<KalendarDayView>(6, 7)

    private var headerBackground: Int = Color.DKGRAY

    private var headerTitleColor: Int = Color.WHITE

    private var todayColor: Int = Color.RED

    private var selectedColor: Int = Color.WHITE

    private var selectedBackground: Int = Color.BLUE

    private var disableColor: Int = Color.GRAY

    private var normalColor: Int = Color.BLACK

    private var eventColor: Int = Color.BLUE

    private var locale: Locale = Locale.getDefault()

    private var currentViewingDate = Date()

    private var currentSelectedView: KalendarDayView? = null

    private var _selectedDate = Date()

    private lateinit var min: Date

    private lateinit var max: Date

    private var disableNoEventDay = true

    private var listener: ((Date)->Unit)? = null

    private var events: List<Event>? = null

    val selectedDate: Date
        get() = _selectedDate

    fun initialize(headerBackground: Int, headerTitleColor: Int, todayColor: Int, selectedColor: Int, selectedBackground: Int, disableColor: Int, normalColor: Int, eventColor: Int) {
        this.headerBackground = headerBackground
        this.headerTitleColor = headerTitleColor
        this.todayColor = todayColor
        this.selectedColor = selectedColor
        this.selectedBackground = selectedBackground
        this.disableColor = disableColor
        this.normalColor = normalColor
        this.eventColor = eventColor
    }

    fun setup(locale: Locale, min: Date, max: Date, selectedDate: Date, disableNoEventDay: Boolean, events: List<Event>?, listener: (Date)->Unit) {
        this.locale = locale
        this.disableNoEventDay = disableNoEventDay
        this.min = min.beginDate()
        this.max = max.endDate()
        this._selectedDate = selectedDate
        this.events = events
        this.listener = listener
        titleTV.setTextColor(headerTitleColor)
        header.setBackgroundColor(headerBackground)
        val shortWeekdays = DateFormatSymbols(locale).shortWeekdays
        firstTV.text = shortWeekdays[1]
        secondTV.text = shortWeekdays[2]
        thirdTV.text = shortWeekdays[3]
        fourthTV.text = shortWeekdays[4]
        fifthTV.text = shortWeekdays[5]
        sixthTV.text = shortWeekdays[6]
        seventhTV.text = shortWeekdays[7]

        reloadView()
    }

    private fun initData() {
        inflate(context, R.layout.kalendar_layout, this)
        header = findViewById(R.id.kalendarHeader)
        backBtn = findViewById(R.id.kalendarBackBtn)
        nextBtn = findViewById(R.id.kalendarNextBtn)
        titleTV = findViewById(R.id.kalendarTitleTV)
        firstTV = findViewById(R.id.kalendarFirstTV)
        secondTV = findViewById(R.id.kalendarSecondTV)
        thirdTV = findViewById(R.id.kalendarThirdTV)
        fourthTV = findViewById(R.id.kalendarFourthTV)
        fifthTV = findViewById(R.id.kalendarFifthTV)
        sixthTV = findViewById(R.id.kalendarSixthTV)
        seventhTV = findViewById(R.id.kalendarSeventhTV)

        backBtn.setOnClickListener {
            moveToPreviousMonth()
        }
        nextBtn.setOnClickListener {
            moveToNextMonth()
        }
        createTable()
    }

    private fun moveToNextMonth() {
        currentViewingDate = currentViewingDate.add(month = 1)
        reloadView()
    }

    private fun moveToPreviousMonth() {
        currentViewingDate = currentViewingDate.add(month = -1)
        reloadView()
    }

    private fun createTable() {
        orientation = VERTICAL
        for (w in 0 until 6) {
            val weekLayout = LinearLayout(context)
            weekLayout.orientation = HORIZONTAL
            weekLayout.weightSum = 7.0f
            var lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
            )
            weekLayout.layoutParams = lp
            addView(weekLayout)
            for (d in 0 until 7) {
                val dayView = KalendarDayView(context,this)
                dayView.id = View.generateViewId()
                lp = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT,
                    1.0f
                )
                weekLayout.addView(dayView, lp)
                days[w, d] = dayView
                dayView.setOnClickListener {
                    if (it != currentSelectedView) {
                        _selectedDate = dayView.date
                        if (!isInMonth(_selectedDate)) {
                            if (_selectedDate > currentViewingDate) {
                                moveToNextMonth()
                            } else {
                                moveToPreviousMonth()
                            }
                        } else {
                            currentSelectedView?.update(false)
                            dayView.update(true)
                            currentSelectedView?.reloadView(true)
                            dayView.reloadView(true)
                            currentSelectedView = dayView
                        }
                        listener?.invoke(_selectedDate)
                    }
                }
            }
        }
    }

    private fun isSelectable(date: Date): Boolean {
        return date in min..max && (!disableNoEventDay || numberOfEvents(date) > 0)
    }

    private fun numberOfEvents(date: Date): Int {
        return events?.filter { wt -> wt.eventDate().isSameDayAs(date) }?.size ?: 0

    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun reloadView() {
        val firstDayOfThisMonth = currentViewingDate.firstDayOfThisMonth()
        var firstDayOfThisWeek = firstDayOfThisMonth
        if (firstDayOfThisWeek.dayOfWeek() > 1) {
            firstDayOfThisWeek = firstDayOfThisWeek.add(day = -(firstDayOfThisWeek.dayOfWeek()-1))
        }
        for (w in 0 until 6) {
            firstDayOfThisWeek = setupWeekDays(firstDayOfThisWeek, w)
        }

        titleTV.text = "${(DateFormatSymbols(locale).months[currentViewingDate.month()]).capitalize()}, ${currentViewingDate.year()}"
        backBtn.visibility =  if (min < currentViewingDate.firstDayOfThisMonth()) View.VISIBLE else View.INVISIBLE
        nextBtn.visibility = if (max > currentViewingDate.lastDayOfThisMonth()) View.VISIBLE else View.INVISIBLE
    }

    private fun setupWeekDays(start: Date, ofWeek: Int): Date {
        var date: Date = start
        val wPos = start.dayOfWeek()-1
        for (pos in wPos until 7) {
            date = start.add(day = pos)
            val dayView = days[ofWeek, pos]
            val isSelected = date.isSameDayAs(selectedDate)
            val isSelectable = isSelectable(date)
            val numberOfEvents = numberOfEvents(date)
            dayView?.update(date, isSelected, isSelectable, numberOfEvents)
            dayView?.reloadView(isSelected)
            if (isSelected) {
                currentSelectedView = dayView
            }
        }
        return date.add(day = 1)
    }

    override fun todayColor(): Int {
        return todayColor
    }

    override fun selectedColor(): Int {
        return selectedColor
    }

    override fun selectedBackgroundColor(): Int {
        return selectedBackground
    }

    override fun disableColor(): Int {
        return disableColor
    }

    override fun normalColor(): Int {
        return normalColor
    }

    override fun eventColor(): Int {
        return eventColor
    }

    override fun isInMonth(date: Date): Boolean {
        return date.isSameMonthAs(currentViewingDate)
    }

}