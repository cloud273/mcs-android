package com.dungnguyen.qdcore.input

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import com.dungnguyen.qdcore.extension.Date
import java.util.*

class DatePickerDialogFragment : InputDialogFragment(), DatePickerDialog.OnDateSetListener {

    private var date: Date? = null
    private var minDate: Date? = null
    private var maxDate: Date? = null
    private var title: String? = null

    private var myTheme = android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
    private var locale = Locale.getDefault()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = GregorianCalendar(locale)
        val pickerDialog = DatePickerDialog(context!!, myTheme,this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        date?.also {
            calendar.time = it
            pickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
        title?.also {
            pickerDialog.setTitle(it)
        }
        minDate?.also {
            pickerDialog.datePicker.minDate = it.time
        }
        maxDate?.also {
            pickerDialog.datePicker.maxDate = it.time
        }
        return pickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener?.onPickerSet(this, Date(day = day, month = month, year = year))
    }

    fun setup(title: String? = null, minDate: Date? = null, maxDate: Date? = null, locale: Locale? = null, theme: Int = android.R.style.Theme_DeviceDefault_Light_Dialog_Alert) {
        this.title = title
        this.minDate = minDate
        this.maxDate = maxDate
        if (locale != null) {
            this.locale = locale
        }
        this.myTheme = theme
    }

    override fun select(data: Any?) {
        this.date = data as Date
    }

}