package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerName(inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PERSON_NAME
        maxTextLength = 128
    }

    override fun getErrorText(): String? {
        if (value != null) {
            val text = value as String
            if (text.isNotEmpty()) {
                return if (!text.matches(Regex(".*[a-zA-Z]+.*")) || text.length < 2) {
                    "Invalid_name".localized
                } else {
                    null
                }
            }
        }
        return super.getErrorText()

    }
}