package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerNote(inputLayout: TextInputLayout, isRequired: Boolean) : McsEditTextControllerDefault(inputLayout, isRequired) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        maxTextLength = 512
    }

    override fun getErrorText(): String? {
        if (value != null) {
            val text = value as String
            if (text.isNotEmpty()) {
                return if (!text.matches(Regex(".*[a-zA-Z]+.*"))) {
                    "Invalid_note".localized
                } else {
                    null
                }
            }
        }
        return super.getErrorText()

    }
}