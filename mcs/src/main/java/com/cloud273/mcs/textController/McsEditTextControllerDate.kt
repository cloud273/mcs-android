package com.cloud273.mcs.textController

import androidx.fragment.app.Fragment
import com.cloud273.mcs.util.toAppDateString
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class McsEditTextControllerDate(inputLayout: TextInputLayout, isRequired: Boolean, title: String, minDate: Date? = null, maxDate: Date? = null, fragment: Fragment, listener: OnSetInterface? = null): McsEditTextControllerCustomKeyboard(inputLayout, isRequired, fragment, listener) {

    init {
        createDateKeyboard(title, minDate, maxDate)
    }

    override fun updateTextField() {
        val date = value as Date
        inputLayout.editText!!.setText(date.toAppDateString())
    }

}