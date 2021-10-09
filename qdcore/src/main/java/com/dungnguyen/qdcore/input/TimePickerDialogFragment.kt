package com.dungnguyen.qdcore.input

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import java.util.*

class TimePickerDialogFragment: InputDialogFragment(), TimePickerDialog.OnTimeSetListener {

    private var time: Date? = null
    private var title: String? = null

    private var myTheme = android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
    private var locale = Locale.getDefault()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = GregorianCalendar(locale)
        val pickerDialog = TimePickerDialog(context!!, myTheme,this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
        time?.also {
            calendar.time = it
            pickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }
        title?.also {
            pickerDialog.setTitle(it)
        }
        return pickerDialog
    }

    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        listener?.onPickerSet(this, calendar.time)
    }

    fun setup(title: String? = null, locale: Locale? = null, theme: Int = android.R.style.Theme_DeviceDefault_Light_Dialog_Alert) {
        this.title = title
        if (locale != null) {
            this.locale = locale
        }
        this.myTheme = theme
    }

    override fun select(data: Any?) {
        this.time = data as Date?
    }

}