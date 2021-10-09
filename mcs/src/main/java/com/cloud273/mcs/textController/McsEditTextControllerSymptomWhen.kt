package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerSymptomWhen (inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT
        maxTextLength = 128
    }

    override fun getErrorText(): String? {
        if (value != null) {
            val text = value as String
            if (text.isNotEmpty()) {
                return if (text.length < 2) {
                    String.format("Too_short_message".localized, 2)
                } else {
                    null
                }
            }
        }
        return super.getErrorText()

    }
}