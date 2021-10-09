package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerAddress(inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
        maxTextLength = 512
    }

    override fun getErrorText(): String? {
        if (value != null) {
            val text = value as String
            if (text.isNotEmpty()) {
                return if (!text.matches(Regex(".*[a-zA-Z]+.*")) || text.length < 2) {
                    "Invalid_address".localized
                } else {
                    null
                }
            }
        }
        return super.getErrorText()

    }
}